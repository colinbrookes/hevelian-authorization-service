package com.hevelian.identity.users.repository;

import com.hevelian.identity.users.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaSpecificationExecutor<User> {

  User findOneByName(String userName);

  int deleteByName(String userName);

}
