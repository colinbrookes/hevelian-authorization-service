package com.hevelian.identity.entitlement.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.hevelian.identity.entitlement.model.pap.PAPPolicy;

public interface PAPPolicyRepository extends PagingAndSortingRepository<PAPPolicy, Long>, JpaSpecificationExecutor<PAPPolicy> {
  PAPPolicy findByPolicyId(String policyId);

  // Need to use the custom Query instead. See:
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=349477
  @Query("select p from PAPPolicy p  where p.policyId in ?1")
  Set<PAPPolicy> findByPolicyIdIsIn(Set<String> policyIds);
}
