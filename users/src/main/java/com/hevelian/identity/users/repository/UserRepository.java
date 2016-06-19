package com.hevelian.identity.users.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hevelian.identity.users.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, String> {
    User findOneByName(String userName);

    int deleteByName(String userName);

    Set<User> findByRoles_Name(String roleName);

    @Modifying
    @Query("update User u set u.password = ?2 where u.name = ?1")
    int changePassword(String userName, String newPassword);
}