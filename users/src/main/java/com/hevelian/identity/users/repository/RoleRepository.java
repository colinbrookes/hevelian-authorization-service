package com.hevelian.identity.users.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.hevelian.identity.users.model.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

  int deleteByName(String roleName);

  Role findOneByName(String roleName);

  // Need to use the custom Query instead. See:
  // https://bugs.eclipse.org/bugs/show_bug.cgi?id=349477
  @Query("select r from Role r  where r.name in ?1")
  Set<Role> findByNameIsIn(Set<String> roleNames);

  @Modifying
  @Query("update Role r set r.name = ?2 where r.name = ?1")
  int updateName(String name, String newName);

  // TODO replace with JPA query
  @Modifying
  @Query(value = "DELETE FROM USER_ROLE WHERE ROLES_ID = ?1", nativeQuery = true)
  void deleteAllUsers(Long roleId);

}
