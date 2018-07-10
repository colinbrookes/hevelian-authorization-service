package com.hevelian.identity.users.repository;

import com.hevelian.identity.users.model.Role;
import com.hevelian.identity.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Set;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
  User findOneByName(String userName);

  int deleteByName(String userName);

  Set<User> findByRoles_Name(String roleName);

  @Modifying
  @Query("update User u set u.password = ?2 where u.name = ?1")
  int changePassword(String userName, String newPassword);

  // Need to use the custom Query instead. See:
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=349477
  @Query("select u from User u  where u.name in ?1 and u.deletable = ?2")
  Set<User> findByNameIsInAndDeletable(Set<String> roleNames, boolean deletable);

  @Query("select u from User u  where u.name like %?1% and u.roles = ?2")
  Page<User> findUsersByFilter(String name, Role roles, Pageable request);

  @Query("select u from User u  where u.name like %?1%")
  Page<User> findUsersByName(String name, Pageable request);

  @Query("select u from User u  where u.roles = ?1")
  Page<User> findUsersByRoles(Role roles, Pageable request);
}
