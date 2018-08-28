package com.hevelian.identity.entitlement.pap.finder;

import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
import com.hevelian.identity.entitlement.pdp.PolicyFactory;
import com.hevelian.identity.entitlement.pdp.PolicyParsingException;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.balana.AbstractPolicy;
import org.wso2.balana.MatchResult;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;
import org.wso2.balana.finder.PolicyFinderResult;

/**
 * Finds policy by PolicyId and verify it on target matching.
 */
public class PAPPolicyFinderModule extends PolicyFinderModule {

  private static final Log log = LogFactory.getLog(PAPPolicyFinderModule.class);
  private PolicyFinder finder = null;
  @Getter
  private PAPPolicy papPolicy;

  public PAPPolicyFinderModule(PAPPolicy papPolicy) {
    this.papPolicy = papPolicy;
  }

  @Override
  public void init(PolicyFinder finder) {
    this.finder = finder;
  }

  @Override
  public boolean isIdReferenceSupported() {
    return true;
  }

  @Override
  public boolean isRequestSupported() {
    return true;
  }

  private AbstractPolicy getAbstractPolicy() {
    AbstractPolicy abstractPolicy = null;
    try {
       abstractPolicy = loadPolicy(getPapPolicy());
    } catch (PolicyParsingException e) {
      log.error("Failed to load policy : " + papPolicy.getPolicyId(), e);
    }
    return abstractPolicy;
  }

  @Override
  public PolicyFinderResult findPolicy(EvaluationCtx context)  {
    PolicyFinderResult policyFinderResult = null;
    AbstractPolicy abstractPolicy = getAbstractPolicy();
    //If policy could not be loaded - just ignore it. Approach is the same as in PDP Policy Finder Module.
    //TODO: consider using runtime exception in both cases.
    if (abstractPolicy == null){
      return new PolicyFinderResult();
    }

    MatchResult match = abstractPolicy.match(context);
    int result = match.getResult();

    if (result == MatchResult.INDETERMINATE) {
      policyFinderResult = new PolicyFinderResult(match.getStatus());
    } else if (result == MatchResult.MATCH) {
      policyFinderResult = new PolicyFinderResult(abstractPolicy);
    } else if (result == MatchResult.NO_MATCH) {
      policyFinderResult = new PolicyFinderResult();
    }
    return policyFinderResult;
  }

  private AbstractPolicy loadPolicy(PAPPolicy papPolicy) throws PolicyParsingException {
    return PolicyFactory.getFactory().getXacmlPolicy(papPolicy.getContent(), finder);
  }

}
