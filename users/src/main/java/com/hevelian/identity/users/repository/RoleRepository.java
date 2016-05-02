package com.hevelian.identity.users.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hevelian.identity.users.model.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    void deleteByName(String roleName);
}