package com.hevelian.identity.entitlement.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.hevelian.identity.entitlement.model.pdp.PDPPolicy;

public interface PDPPolicyRepository extends PagingAndSortingRepository<PDPPolicy, Long> {

}
