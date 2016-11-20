package com.hevelian.identity.entitlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.hevelian.identity.core.SystemRoles;
import com.hevelian.identity.core.exc.EntityNotFoundByCriteriaException;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;
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

    @Transactional
    public PAPPolicy addPolicy(PAPPolicy policy) {
        Preconditions.checkArgument(policy.getId() == null);
        papPolicyRepository.save(policy);
        return policy;
    }

    @Transactional
    public PAPPolicy updatePolicy(PAPPolicy policy) throws PolicyNotFoundByPolicyIdException {
        Preconditions.checkArgument(policy.getId() == null);
        PAPPolicy policyEntity = getPolicy(policy.getPolicyId());

        policyEntity.setContent(policy.getContent());
        // TODO set policy ID from content and other properties
        papPolicyRepository.save(policyEntity);
        return policyEntity;
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

    public static class PolicyNotFoundByPolicyIdException
            extends EntityNotFoundByCriteriaException {
        private static final long serialVersionUID = 8894276524310804961L;

        public PolicyNotFoundByPolicyIdException(String policyId) {
            super("policyId", policyId);
        }
    }
}
