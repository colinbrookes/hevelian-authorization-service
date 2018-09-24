package com.hevelian.identity.entitlement.pdp.finder;

import com.hevelian.identity.entitlement.PDPService;
import com.hevelian.identity.entitlement.exc.PolicyRetrievalException;
import com.hevelian.identity.entitlement.finder.ISPolicyFinderModule;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import com.hevelian.identity.entitlement.exc.PolicyParsingException;
import lombok.extern.log4j.Log4j2;
import org.wso2.balana.AbstractPolicy;
import org.wso2.balana.MatchResult;
import org.wso2.balana.PolicySet;
import org.wso2.balana.combine.PolicyCombiningAlgorithm;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.ctx.Status;
import org.wso2.balana.finder.PolicyFinderResult;

import java.net.URI;
import java.util.*;

/**
 * A test PDP finder module. Reloads policies on every request. Just for test purposes.
 */
@Log4j2
public class PDPPolicyFinderModule extends ISPolicyFinderModule {

  private PDPService pdpService;

  public PDPPolicyFinderModule(PDPService pdpService) {
    this.pdpService = pdpService;
  }

  @Override
  public PolicyFinderResult findPolicy(EvaluationCtx context) {
    PolicyCombiningAlgorithm combiningAlg = pdpService.getPolicyCombiningAlgorithmInstance();
    Map<URI, AbstractPolicy> policies;
    try{
      policies = getPolicies();
    } catch (PolicyRetrievalException e) {
      // just only logs, since the probability is super low.
      log.error("Failed to load policy." , e);
      //If policy could not be loaded - just ignore it. Approach is the same as in PAP Policy Finder Module.
      //TODO: consider using runtime exception in three cases.
      policies = new HashMap<>();
    }

    ArrayList<AbstractPolicy> selectedPolicies = new ArrayList<>();
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

        if ((combiningAlg == null) && (!selectedPolicies.isEmpty())) {
          // we found a match before, so this is an error
          ArrayList<String> code = new ArrayList<>();
          code.add(Status.STATUS_PROCESSING_ERROR);
          Status status = new Status(code, "Too many applicable " + "top-level policies. Verify if PDP combining algorithm is set.");
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
        return new PolicyFinderResult(selectedPolicies.get(0));
      default:
        return new PolicyFinderResult(new PolicySet(null, combiningAlg, null, selectedPolicies));
    }
  }

  @Override
  public Map<URI, AbstractPolicy> getPolicies() throws PolicyRetrievalException {
    Map<URI, AbstractPolicy> policies = new LinkedHashMap<>();
    for (PDPPolicy pdpPolicy : pdpService.getAllPoliciesOrdered()) {
      AbstractPolicy abstractPolicy;
      try {
        abstractPolicy = loadPolicy(pdpPolicy);
        policies.put(abstractPolicy.getId(), abstractPolicy);
      } catch (PolicyParsingException e) {
        throw new PolicyRetrievalException("Failed to load policy : " + pdpPolicy.getPolicyId(), e);
      }
    }
    return policies;
  }
}
