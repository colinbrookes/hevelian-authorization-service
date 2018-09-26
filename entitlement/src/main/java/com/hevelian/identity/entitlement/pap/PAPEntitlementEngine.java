package com.hevelian.identity.entitlement.pap;

import com.hevelian.identity.entitlement.engine.EntitlementEngine;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
import com.hevelian.identity.entitlement.pap.finder.PAPPolicyFinderModule;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;

import java.util.HashSet;
import java.util.Set;

/**
 * Determines a response by content or attributes of the PAP policy.
 */
public class PAPEntitlementEngine extends EntitlementEngine {
    private PAPPolicy papPolicy;
  /**
   * Creates {@link EntitlementEngine} instance for evaluating decision using only one PAP Policy.
   *
   * @param papPolicy PAP Policy instance.
   */
  public PAPEntitlementEngine(PAPPolicy papPolicy) {
    this.papPolicy = papPolicy;
    init();
  }

  @Override
  protected PolicyFinder getPolicyFinder() {
    PolicyFinder policyFinder = new PolicyFinder();
    Set<PolicyFinderModule> policyFinderModules = new HashSet<>();
    PAPPolicyFinderModule inMemoryPolicyFinderModule = new PAPPolicyFinderModule(papPolicy);
    policyFinderModules.add(inMemoryPolicyFinderModule);
    policyFinder.setModules(policyFinderModules);
    return policyFinder;
  }
}

