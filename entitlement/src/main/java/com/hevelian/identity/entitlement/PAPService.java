package com.hevelian.identity.entitlement;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wso2.balana.AbstractPolicy;
import org.wso2.balana.Policy;
import org.wso2.balana.PolicySet;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.entitlement.exc.PoliciesNotFoundByPolicyIdsException;
import com.hevelian.identity.entitlement.exc.PolicyNotFoundByPolicyIdException;
import com.hevelian.identity.entitlement.model.PolicyType;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import com.hevelian.identity.entitlement.pdp.PolicyFactory;
import com.hevelian.identity.entitlement.pdp.PolicyParsingException;
import com.hevelian.identity.entitlement.repository.PAPPolicyRepository;
import com.hevelian.identity.entitlement.repository.PDPPolicyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@Secured(value = SystemRoles.TENANT_ADMIN)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PAPService {
  @Getter
  private final PAPPolicyRepository papPolicyRepository;
  @Getter
  private final PDPPolicyRepository pdpPolicyRepository;

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
  public PAPPolicy updatePolicy(String policyContent) throws PolicyParsingException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(policyContent));
    PAPPolicy policy = contentToPolicy(policyContent);
    return updatePolicy(policy);
  }

  @Transactional(readOnly = false)
  public PAPPolicy updatePolicy(PAPPolicy policy) {
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

  @Transactional(readOnly = false)
  public void deletePolicy(String policyId) throws PAPPolicyNotFoundByPolicyIdException {
    PAPPolicy policy = getPolicy(policyId);
    papPolicyRepository.delete(policy);
  }

  public PAPPolicy getPolicy(String policyId) throws PAPPolicyNotFoundByPolicyIdException {
    PAPPolicy policy = papPolicyRepository.findByPolicyId(policyId);
    if (policy == null)
      throw new PAPPolicyNotFoundByPolicyIdException(policyId);
    return policy;
  }

  public Iterable<String> getPolicyVersions(String policyId) {
    return null;
  }

  @Transactional(readOnly = false)
  public Set<PDPPolicy> publishToPDP(Set<String> policyIds, Boolean enabled, Integer order)
      throws PAPPoliciesNotFoundByPolicyIdsException {
    Set<PAPPolicy> papPolicies = papPolicyRepository.findByPolicyIdIsIn(policyIds);
    if (papPolicies.size() != policyIds.size()) {
      throw new PAPPoliciesNotFoundByPolicyIdsException(Sets.difference(policyIds,
          papPolicies.stream().map(p -> p.getPolicyId()).collect(Collectors.toSet())));
    }

    Set<PDPPolicy> pdpPolicies = new LinkedHashSet<>();
    for (PAPPolicy papPolicy : papPolicies) {
      PDPPolicy pdpPolicy = pdpPolicyRepository.findByPolicyId(papPolicy.getPolicyId());
      if (pdpPolicy == null) {
        pdpPolicy = new PDPPolicy();
        pdpPolicy.setPolicyId(papPolicy.getPolicyId());
        pdpPolicy.setPolicyOrder(0);
      }
      pdpPolicy.setContent(papPolicy.getContent());
      pdpPolicy.setPolicyType(papPolicy.getPolicyType());
      pdpPolicy.setPolicyOrder(order == null ? pdpPolicy.getPolicyOrder() : order);

      pdpPolicies.add(pdpPolicy);
    }
    pdpPolicyRepository.save(pdpPolicies);
    return pdpPolicies;
  }

  public static class PAPPolicyNotFoundByPolicyIdException
      extends PolicyNotFoundByPolicyIdException {
    private static final long serialVersionUID = -2762669551702182403L;

    public PAPPolicyNotFoundByPolicyIdException(String policyId) {
      super(policyId);
    }
  }

  public static class PAPPoliciesNotFoundByPolicyIdsException
      extends PoliciesNotFoundByPolicyIdsException {
    private static final long serialVersionUID = 5535351893028459277L;

    public PAPPoliciesNotFoundByPolicyIdsException(Set<String> policyIds) {
      super(policyIds);
    }
  }


}
