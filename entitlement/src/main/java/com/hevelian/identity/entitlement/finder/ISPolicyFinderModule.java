package com.hevelian.identity.entitlement.finder;

import com.hevelian.identity.entitlement.exc.PolicyRetrievalException;
import com.hevelian.identity.entitlement.pdp.PolicyFactory;
import com.hevelian.identity.entitlement.exc.PolicyParsingException;
import lombok.extern.log4j.Log4j2;
import org.wso2.balana.*;
import org.wso2.balana.ctx.Status;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;
import org.wso2.balana.finder.PolicyFinderResult;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public abstract class ISPolicyFinderModule extends PolicyFinderModule {
  private PolicyFinder finder = null;

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
  public PolicyFinderResult findPolicy(URI idReference, int type, VersionConstraints constraints,
                                       PolicyMetaData parentMetaData) {
    Map<URI, AbstractPolicy> policies;
    try {
      policies = getPolicies();
    } catch (PolicyRetrievalException e) {
      // just only logs, since the probability is super low.
      log.error("Failed to load policy" , e);
      //If policy could not be loaded - just ignore it. Approach is the same as in PDP and PAP Policy Finder Modules.
      //TODO: consider using runtime exception in three cases.
      policies = new HashMap<>();
    }
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
    ArrayList<String> code = new ArrayList<>();
    code.add(Status.STATUS_PROCESSING_ERROR);
    Status status = new Status(code, "couldn't load referenced policy");
    log.info("No policy found, code=" + code);
    return new PolicyFinderResult(status);
  }

  /**
   * This method must find policies in corresponding repository.
   *
   * @return the map of policies.
   * @throws PolicyRetrievalException if can not load policy.
   */
  abstract public Map<URI, AbstractPolicy> getPolicies() throws PolicyRetrievalException;

  /**
   * Converts a {@link com.hevelian.identity.entitlement.model.Policy} into {@link AbstractPolicy} class object.
   *
   * @param policy the policy object.
   * @return  the {@link AbstractPolicy} object.
   * @throws PolicyParsingException if can not load policy.
   */
  protected AbstractPolicy loadPolicy(com.hevelian.identity.entitlement.model.Policy policy) throws PolicyParsingException {
    return PolicyFactory.getFactory().getXacmlPolicy(policy.getContent(), finder);
  }

}
