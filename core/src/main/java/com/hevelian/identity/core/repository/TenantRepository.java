package com.hevelian.identity.core.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hevelian.identity.core.model.Tenant;

@Transactional(propagation = Propagation.MANDATORY)
public interface TenantRepository extends PagingAndSortingRepository<Tenant, Long> {
    Tenant findByDomain(String tenantDomain);
}