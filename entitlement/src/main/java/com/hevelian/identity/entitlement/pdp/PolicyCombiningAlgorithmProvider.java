package com.hevelian.identity.entitlement.pdp;

import org.springframework.stereotype.Component;
import org.wso2.balana.combine.PolicyCombiningAlgorithm;
import org.wso2.balana.combine.xacml2.FirstApplicablePolicyAlg;
import org.wso2.balana.combine.xacml2.OnlyOneApplicablePolicyAlg;
import org.wso2.balana.combine.xacml3.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class PolicyCombiningAlgorithmProvider {
  protected Map<String, PolicyCombiningAlgorithm> algorithms;

  @PostConstruct
  public void init() {
    Map<String, PolicyCombiningAlgorithm> policyCombAlg = new HashMap<>();
    policyCombAlg.put(DenyOverridesPolicyAlg.algId, new DenyOverridesPolicyAlg());
    policyCombAlg.put(PermitOverridesPolicyAlg.algId, new PermitOverridesPolicyAlg());
    policyCombAlg.put(FirstApplicablePolicyAlg.algId, new FirstApplicablePolicyAlg());
    policyCombAlg.put(PermitUnlessDenyPolicyAlg.algId, new PermitUnlessDenyPolicyAlg());
    policyCombAlg.put(DenyUnlessPermitPolicyAlg.algId, new DenyUnlessPermitPolicyAlg());
    policyCombAlg.put(OrderedPermitOverridesPolicyAlg.algId, new OrderedPermitOverridesPolicyAlg());
    policyCombAlg.put(OrderedDenyOverridesPolicyAlg.algId, new OrderedDenyOverridesPolicyAlg());
    policyCombAlg.put(OnlyOneApplicablePolicyAlg.algId, new OnlyOneApplicablePolicyAlg());
    this.algorithms = policyCombAlg;
  }

  public PolicyCombiningAlgorithm getPolicyCombiningAlgorithm(String policyCombiningAlgorithmId) throws PolicyCombiningAlgorithmNotSupportedException {
    if (!isPolicyCombiningAlgorithmSupported(policyCombiningAlgorithmId)) {
      throw new PolicyCombiningAlgorithmNotSupportedException(policyCombiningAlgorithmId);
    }
    return algorithms.get(policyCombiningAlgorithmId);
  }

  public Set<String> getSupportedPolicyCombiningAlgorithmIds() {
    if (algorithms == null) {
      throw new IllegalStateException("Policy Combining Algorithm Provider is not initialized. Call an init() method first.");
    }
    return algorithms.keySet();
  }

  public boolean isPolicyCombiningAlgorithmSupported(String policyCombiningAlgorithmId) {
    return getSupportedPolicyCombiningAlgorithmIds().contains(policyCombiningAlgorithmId);
  }

}
