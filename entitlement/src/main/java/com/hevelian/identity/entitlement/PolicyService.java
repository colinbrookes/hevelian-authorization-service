package com.hevelian.identity.entitlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hevelian.identity.entitlement.repository.PolicyRepository;

@Service
public class PolicyService {
    private final PolicyRepository policyRepository;

    @Autowired
    public PolicyService(PolicyRepository policyRepository) {
        super();
        this.policyRepository = policyRepository;
    }

    public PolicyRepository getPolicyRepository() {
        return policyRepository;
    }
}
