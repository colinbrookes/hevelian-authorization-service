package com.hevelian.identity.entitlement.pdp;

import com.hevelian.identity.entitlement.PDPService;
import com.hevelian.identity.entitlement.engine.EntitlementEngine;
import com.hevelian.identity.entitlement.pdp.finder.PDPPolicyFinderModule;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;

import java.util.HashSet;
import java.util.Set;

@Component
@Scope("singleton")
public class PDPEntitlementEngine extends EntitlementEngine {
  private PDPService pdpService;

  /**
   * Configures PDP object for searching and evaluating all PDP Policies.
   *
   * @param pdpService PDP Service instance.
   */
  public PDPEntitlementEngine(PDPService pdpService) {
    this.pdpService = pdpService;
    init();
  }

  @Override
  protected PolicyFinder getPolicyFinder() {
    PolicyFinder policyFinder = new PolicyFinder();
    Set<PolicyFinderModule> policyFinderModules = new HashSet<>();
    PDPPolicyFinderModule inMemoryPolicyFinderModule = new PDPPolicyFinderModule(pdpService);

    policyFinderModules.add(inMemoryPolicyFinderModule);
    policyFinder.setModules(policyFinderModules);
    return policyFinder;
  }

}

