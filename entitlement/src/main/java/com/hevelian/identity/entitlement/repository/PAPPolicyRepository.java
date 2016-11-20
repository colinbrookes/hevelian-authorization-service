package com.hevelian.identity.entitlement.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hevelian.identity.entitlement.model.pap.PAPPolicy;

public interface PAPPolicyRepository extends PagingAndSortingRepository<PAPPolicy, Long> {
    PAPPolicy findByPolicyId(String policyId);
}
