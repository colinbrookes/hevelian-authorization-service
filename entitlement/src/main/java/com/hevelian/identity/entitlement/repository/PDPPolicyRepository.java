package com.hevelian.identity.entitlement.repository;

import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PDPPolicyRepository extends PagingAndSortingRepository<PDPPolicy, Long>, JpaSpecificationExecutor<PDPPolicy> {
  PDPPolicy findByPolicyId(String policyId);
}
