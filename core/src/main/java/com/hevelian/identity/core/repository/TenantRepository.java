package com.hevelian.identity.core.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hevelian.identity.core.model.Tenant;

//Make the propagation mandatory to be sure that transaction management is handled in higher layers (where the business logic occurs).
@Transactional(propagation = Propagation.MANDATORY)
public interface TenantRepository extends PagingAndSortingRepository<Tenant, Long> {

    @Modifying
    int deleteByDomain(String tenantDomain);

    Tenant findByDomain(String tenantDomain);
}