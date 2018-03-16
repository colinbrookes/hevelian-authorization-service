package com.hevelian.identity.entitlement.pdp.finder;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.balana.AbstractPolicy;
import org.wso2.balana.MatchResult;
import org.wso2.balana.Policy;
import org.wso2.balana.PolicyMetaData;
import org.wso2.balana.PolicyReference;
import org.wso2.balana.PolicySet;
import org.wso2.balana.VersionConstraints;
import org.wso2.balana.combine.PolicyCombiningAlgorithm;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.ctx.Status;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;
import org.wso2.balana.finder.PolicyFinderResult;
import com.hevelian.identity.entitlement.PDPService;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import com.hevelian.identity.entitlement.pdp.PolicyFactory;
import com.hevelian.identity.entitlement.pdp.PolicyParsingException;

/**
 * A test finder module. Reloads policies on every request. Just for test purposes.
 */
public class PDPPolicyFinderModule extends PolicyFinderModule {

  private PolicyFinder finder = null;
  private PolicyCombiningAlgorithm combiningAlg;
  private PDPService pdpService;
  private static Log log = LogFactory.getLog(PDPPolicyFinderModule.class);

  public PDPPolicyFinderModule(PDPService pdpService, PolicyCombiningAlgorithm combiningAlg) {
    this.pdpService = pdpService;
    this.combiningAlg = combiningAlg;
  }

  private Map<URI, AbstractPolicy> getPolicies() {
    Map<URI, AbstractPolicy> policies = new LinkedHashMap<URI, AbstractPolicy>();
    for (PDPPolicy d : pdpService.getAllPolicies()) {
      AbstractPolicy p;
      try {
        p = loadPolicy(d);
        policies.put(p.getId(), p);
      } catch (PolicyParsingException e) {
        // just only logs
        log.error("Failed to load policy : " + d.getPolicyId(), e);
      }
    }
    return policies;
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

  @Override
  public PolicyFinderResult findPolicy(EvaluationCtx context) {
    Map<URI, AbstractPolicy> policies = getPolicies();
    ArrayList<AbstractPolicy> selectedPolicies = new ArrayList<AbstractPolicy>();
    Set<Map.Entry<URI, AbstractPolicy>> entrySet = policies.entrySet();

    // iterate through all the policies we currently have loaded
    for (Map.Entry<URI, AbstractPolicy> entry : entrySet) {
      AbstractPolicy policy = entry.getValue();
      MatchResult match = policy.match(context);
      int result = match.getResult();

      // if target matching was indeterminate, then return the error
      if (result == MatchResult.INDETERMINATE)
        return new PolicyFinderResult(match.getStatus());

      // see if the target matched
      if (result == MatchResult.MATCH) {

        if ((combiningAlg == null) && (selectedPolicies.size() > 0)) {
          // we found a match before, so this is an error
          ArrayList<String> code = new ArrayList<String>();
          code.add(Status.STATUS_PROCESSING_ERROR);
          Status status = new Status(code, "too many applicable " + "top-level policies");
          return new PolicyFinderResult(status);
        }

        // this is the first match we've found, so remember it
        selectedPolicies.add(policy);
      }
    }

    // no errors happened during the search, so now take the right
    // action based on how many policies we found
    switch (selectedPolicies.size()) {
      case 0:
        if (log.isDebugEnabled()) {
          log.debug("No matching XACML policy found");
        }
        return new PolicyFinderResult();
      case 1:
        return new PolicyFinderResult((selectedPolicies.get(0)));
      default:
        return new PolicyFinderResult(new PolicySet(null, combiningAlg, null, selectedPolicies));
    }
  }

  @Override
  public PolicyFinderResult findPolicy(URI idReference, int type, VersionConstraints constraints,
      PolicyMetaData parentMetaData) {
    Map<URI, AbstractPolicy> policies = getPolicies();
    AbstractPolicy policy = policies.get(idReference);
    if (policy != null) {
      if (type == PolicyReference.POLICY_REFERENCE) {
        if (policy instanceof Policy) {
          return new PolicyFinderResult(policy);
        }
      } else {
        if (policy instanceof PolicySet) {
          return new PolicyFinderResult(policy);
        }
      }
    }

    // if there was an error loading the policy, return the error
    ArrayList<String> code = new ArrayList<String>();
    code.add(Status.STATUS_PROCESSING_ERROR);
    Status status = new Status(code, "couldn't load referenced policy");
    log.info("No policy found, code=" + code);
    return new PolicyFinderResult(status);
  }

  private AbstractPolicy loadPolicy(PDPPolicy pdpPolicy) throws PolicyParsingException {
    return PolicyFactory.getFactory().getXacmlPolicy(pdpPolicy.getContent(), finder);
  }

}
