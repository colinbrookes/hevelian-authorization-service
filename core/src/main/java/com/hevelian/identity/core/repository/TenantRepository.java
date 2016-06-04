package com.hevelian.identity.core.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hevelian.identity.core.model.Tenant;

//Make the propagation mandatory to be sure that transaction management is handled in higher layers (where the business logic occurs).
@Transactional(propagation = Propagation.MANDATORY)
public interface TenantRepository extends PagingAndSortingRepository<Tenant, Long> {

    @Modifying
    @Query("update Tenant t set t.active = ?2 where t.domain = ?1")
    int setActive(String tenantDomain, boolean active);

    @Modifying
    int deleteByDomain(String tenantDomain);

    Tenant findByDomain(String tenantDomain);
}