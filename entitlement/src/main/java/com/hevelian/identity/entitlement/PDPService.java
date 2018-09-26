package com.hevelian.identity.entitlement;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.entitlement.exc.PoliciesNotFoundByPolicyIdsException;
import com.hevelian.identity.entitlement.exc.PolicyNotFoundByPolicyIdException;
import com.hevelian.identity.entitlement.model.pdp.PDPConfig;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import com.hevelian.identity.entitlement.pdp.PolicyCombiningAlgorithmNotSupportedException;
import com.hevelian.identity.entitlement.pdp.PolicyCombiningAlgorithmProvider;
import com.hevelian.identity.entitlement.repository.PDPConfigRepository;
import com.hevelian.identity.entitlement.repository.PDPPolicyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wso2.balana.combine.PolicyCombiningAlgorithm;
import org.wso2.balana.combine.xacml3.DenyOverridesPolicyAlg;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@Secured(value = SystemRoles.TENANT_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PDPService {

  public static final String DEFAULT_POLICY_COMBINING_ALGORITHM_ID = DenyOverridesPolicyAlg.algId;
  @Getter
  private final PDPPolicyRepository policyRepository;
  @Getter
  private final PDPConfigRepository pdpConfigRepository;
  @Getter
  private final PolicyCombiningAlgorithmProvider combiningAlgorithmProvider;

  public Iterable<PDPPolicy> getAllPoliciesOrdered() {
    Sort sort = new Sort(Sort.Direction.ASC, PDPPolicy.FIELD_POLICY_ORDER);
    return policyRepository.findAll(sort);
  }

  public Page<PDPPolicy> searchPolicies(Specification<PDPPolicy> spec, PageRequest request) {
    return policyRepository.findAll(spec, request);
  }

  public PDPPolicy getPolicy(String policyId) throws PDPPolicyNotFoundByPolicyIdException {
    PDPPolicy policy = policyRepository.findByPolicyId(policyId);
    if (policy == null)
      throw new PDPPolicyNotFoundByPolicyIdException(policyId);
    return policy;
  }

  public PolicyCombiningAlgorithm getPolicyCombiningAlgorithmInstance() {
    String algorithm = getPolicyCombiningAlgorithm();
    try {
      return combiningAlgorithmProvider.getPolicyCombiningAlgorithm(algorithm);
    } catch (PolicyCombiningAlgorithmNotSupportedException e) {
      throw new PDPIntegrityException(String.format("Policy Combining Algorithm with id '%s' not supported. PDP config is broken.", algorithm), e);
    }
  }

  public String getPolicyCombiningAlgorithm() {
    PDPConfig pdpConfig = findPDPConfig();
    return (pdpConfig == null) ? DEFAULT_POLICY_COMBINING_ALGORITHM_ID : pdpConfig.getPolicyCombiningAlgorithm();
  }

  @Transactional(readOnly = false)
  public void deletePolicy(String policyId) throws PDPPolicyNotFoundByPolicyIdException {
    PDPPolicy policy = getPolicy(policyId);
    policyRepository.delete(policy);
  }

  @Transactional(readOnly = false)
  public void enableDisablePolicy(String policyId, boolean enable)
      throws PDPPolicyNotFoundByPolicyIdException {
    PDPPolicy policy = getPolicy(policyId);
    policy.setEnabled(enable);
    policyRepository.save(policy);
  }

  @Transactional(readOnly = false)
  public void orderPolicy(String policyId, int order) throws PDPPolicyNotFoundByPolicyIdException {
    Preconditions.checkArgument(order >= 0);
    PDPPolicy policy = getPolicy(policyId);
    policy.setPolicyOrder(order);
    policyRepository.save(policy);
  }

  @Transactional(readOnly = false)
  public PDPConfig setPolicyCombiningAlgorithm(String algorithm) throws PDPPolicyCombiningAlgorithmNotSupportedException {
    if (!combiningAlgorithmProvider.isPolicyCombiningAlgorithmSupported(algorithm)) {
      throw new PDPPolicyCombiningAlgorithmNotSupportedException(algorithm);
    }
    PDPConfig pdpConfig = findPDPConfig();
    if (pdpConfig == null) {
      pdpConfig = new PDPConfig();
    }
    pdpConfig.setPolicyCombiningAlgorithm(algorithm);
    pdpConfigRepository.save(pdpConfig);
    return pdpConfig;
  }

  private PDPConfig findPDPConfig() {
    Iterable<PDPConfig> all = pdpConfigRepository.findAll();
    return Iterables.getOnlyElement(all, null);
  }

  public static class PDPPolicyNotFoundByPolicyIdException
      extends PolicyNotFoundByPolicyIdException {
    private static final long serialVersionUID = -5729938846638712240L;

    public PDPPolicyNotFoundByPolicyIdException(String policyId) {
      super(policyId);
    }
  }

  public static class PDPPoliciesNotFoundByPolicyIdsException
      extends PoliciesNotFoundByPolicyIdsException {
    private static final long serialVersionUID = -6313980272314121000L;

    public PDPPoliciesNotFoundByPolicyIdsException(Set<String> policyIds) {
      super(policyIds);
    }
  }

  public class PDPPolicyCombiningAlgorithmNotSupportedException
      extends Exception {
    private static final long serialVersionUID = 6422332522711048209L;
    @Getter
    private final String policyCombiningAlgorithmId;

    public PDPPolicyCombiningAlgorithmNotSupportedException(String policyCombiningAlgorithmId) {
      super(String.format("PDP does not support Policy Combining Algorithm '%s'.", policyCombiningAlgorithmId));
      this.policyCombiningAlgorithmId = policyCombiningAlgorithmId;
    }
  }

  public class PDPIntegrityException
      extends RuntimeException {
    private static final long serialVersionUID = -3210219910685698340L;

    public PDPIntegrityException(String message, Exception e) {
      super(message, e);
    }
  }
}
