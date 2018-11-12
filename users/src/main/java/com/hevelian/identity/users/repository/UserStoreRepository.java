package com.hevelian.identity.users.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.hevelian.identity.users.model.UserStore;

public interface UserStoreRepository extends PagingAndSortingRepository<UserStore, String> {
  UserStore findOneByDomain(String domain);
}
