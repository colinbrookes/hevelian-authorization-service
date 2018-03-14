package com.hevelian.identity.entitlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wso2.balana.AbstractPolicy;
import org.wso2.balana.Policy;
import org.wso2.balana.PolicySet;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.core.exc.EntityNotFoundByCriteriaException;
import com.hevelian.identity.entitlement.model.PolicyType;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
import com.hevelian.identity.entitlement.pdp.PolicyFactory;
import com.hevelian.identity.entitlement.pdp.PolicyParsingException;
import com.hevelian.identity.entitlement.repository.PAPPolicyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@Secured(value = SystemRoles.TENANT_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PAPService {
  @Getter
  private final PAPPolicyRepository papPolicyRepository;

  public Iterable<PAPPolicy> getAllPolicies() {
    return papPolicyRepository.findAll();
  }

  @Transactional(readOnly = false)
  public PAPPolicy addPolicy(String policyContent) throws PolicyParsingException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(policyContent));
    PAPPolicy policy = contentToPolicy(policyContent);
    return addPolicy(policy);
  }

  @Transactional(readOnly = false)
  public PAPPolicy addPolicy(PAPPolicy policy) {
    Preconditions.checkArgument(policy.getId() == null);
    papPolicyRepository.save(policy);
    return policy;
  }

  @Transactional(readOnly = false)
  public PAPPolicy updatePolicy(String policyContent)
      throws PolicyParsingException, PolicyNotFoundByPolicyIdException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(policyContent));
    PAPPolicy policy = contentToPolicy(policyContent);
    return updatePolicy(policy);
  }

  @Transactional(readOnly = false)
  public PAPPolicy updatePolicy(PAPPolicy policy) throws PolicyNotFoundByPolicyIdException {
    Preconditions.checkArgument(policy.getId() == null);
    PAPPolicy policyEntity = papPolicyRepository.findByPolicyId(policy.getPolicyId());
    if (policyEntity != null) {
      policyEntity.setContent(policy.getContent());
      policyEntity.setPolicyType(policy.getPolicyType());
    }
    // If policyId is not the same - create a new policy instead.
    else {
      policyEntity = policy;
    }
    papPolicyRepository.save(policyEntity);
    return policyEntity;
  }

  private PAPPolicy contentToPolicy(String policyContent) throws PolicyParsingException {
    AbstractPolicy ap = PolicyFactory.getFactory().getXacmlPolicy(policyContent);
    Preconditions.checkArgument(ap instanceof Policy || ap instanceof PolicySet);
    PAPPolicy policy = new PAPPolicy();
    policy.setContent(ap.encode());
    policy.setPolicyId(ap.getId().toString());
    policy.setPolicyType(ap instanceof Policy ? PolicyType.POLICY : PolicyType.POLICY_SET);
    return policy;
  }

  @Transactional
  public void deletePolicy(String policyId) throws PolicyNotFoundByPolicyIdException {
    PAPPolicy policy = getPolicy(policyId);
    papPolicyRepository.delete(policy);
  }

  private PAPPolicy getPolicy(String policyId) throws PolicyNotFoundByPolicyIdException {
    PAPPolicy policy = papPolicyRepository.findByPolicyId(policyId);
    if (policy == null)
      throw new PolicyNotFoundByPolicyIdException(policyId);
    return policy;
  }

  public Iterable<String> getPolicyVersions(String policyId) {
    return null;
  }

  public static class PolicyNotFoundByPolicyIdException extends EntityNotFoundByCriteriaException {
    private static final long serialVersionUID = 8894276524310804961L;

    public PolicyNotFoundByPolicyIdException(String policyId) {
      super("policyId", policyId);
    }
  }
}
