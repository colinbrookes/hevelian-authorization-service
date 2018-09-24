package com.hevelian.identity.entitlement.pap.finder;

import com.google.common.collect.Iterables;
import com.hevelian.identity.entitlement.exc.PolicyRetrievalException;
import com.hevelian.identity.entitlement.finder.ISPolicyFinderModule;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
import com.hevelian.identity.entitlement.exc.PolicyParsingException;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.wso2.balana.AbstractPolicy;
import org.wso2.balana.MatchResult;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.finder.PolicyFinderResult;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Finds policy by PolicyId and verify it on target matching.
 */
@Log4j2
public class PAPPolicyFinderModule extends ISPolicyFinderModule {
  @Getter
  private PAPPolicy papPolicy;

  public PAPPolicyFinderModule(PAPPolicy papPolicy) {
    this.papPolicy = papPolicy;
  }

  @Override
  public PolicyFinderResult findPolicy(EvaluationCtx context) {
    AbstractPolicy policy;
    try {
      policy = Iterables.getOnlyElement(getPolicies().entrySet()).getValue();
    }
    catch (PolicyRetrievalException e){
      // just only logs, since the probability is super low.
      log.error("Failed to load policy : " + papPolicy.getPolicyId(), e);
      //If policy could not be loaded - just ignore it. Approach is the same as in PDP Policy Finder Module.
      //TODO: consider using runtime exception in three cases.
      return new PolicyFinderResult();
    }

    MatchResult match = policy.match(context);
    int result = match.getResult();

    PolicyFinderResult policyFinderResult = null;
    if (result == MatchResult.INDETERMINATE) {
      policyFinderResult = new PolicyFinderResult(match.getStatus());
    } else if (result == MatchResult.MATCH) {
      policyFinderResult = new PolicyFinderResult(policy);
    } else if (result == MatchResult.NO_MATCH) {
      policyFinderResult = new PolicyFinderResult();
    }
    return policyFinderResult;
  }

  @Override
  public Map<URI, AbstractPolicy> getPolicies() throws PolicyRetrievalException {
    Map<URI, AbstractPolicy> policies = new LinkedHashMap<>();
    AbstractPolicy abstractPolicy;
    try {
      abstractPolicy = loadPolicy(papPolicy);
      policies.put(abstractPolicy.getId(), abstractPolicy);
    } catch (PolicyParsingException e) {
      throw new PolicyRetrievalException("Failed to load policy : " + papPolicy.getPolicyId(), e);
    }
    return policies;
  }

}
