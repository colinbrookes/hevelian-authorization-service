package com.hevelian.identity.entitlement;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.base.Preconditions;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.entitlement.exc.PoliciesNotFoundByPolicyIdsException;
import com.hevelian.identity.entitlement.exc.PolicyNotFoundByPolicyIdException;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import com.hevelian.identity.entitlement.repository.PDPPolicyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@Secured(value = SystemRoles.TENANT_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PDPService {
  @Getter
  private final PDPPolicyRepository policyRepository;

  public Iterable<PDPPolicy> getAllPolicies() {
    return policyRepository.findAll();
  }

  public PDPPolicy getPolicy(String policyId) throws PDPPolicyNotFoundByPolicyIdException {
    PDPPolicy policy = policyRepository.findByPolicyId(policyId);
    if (policy == null)
      throw new PDPPolicyNotFoundByPolicyIdException(policyId);
    return policy;
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
}
