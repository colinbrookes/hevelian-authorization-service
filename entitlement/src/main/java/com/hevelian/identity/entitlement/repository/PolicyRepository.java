package com.hevelian.identity.entitlement.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hevelian.identity.entitlement.model.Policy;

public interface PolicyRepository extends PagingAndSortingRepository<Policy, Long> {

}
