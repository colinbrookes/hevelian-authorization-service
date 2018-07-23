package com.hevelian.identity.core.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hevelian.identity.core.model.Tenant;

@Transactional(propagation = Propagation.MANDATORY)
public interface TenantRepository extends PagingAndSortingRepository<Tenant, Long>, JpaSpecificationExecutor<Tenant> {
  Tenant findByDomain(String tenantDomain);
}
