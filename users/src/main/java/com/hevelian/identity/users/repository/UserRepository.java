package com.hevelian.identity.users.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hevelian.identity.users.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    void deleteByName(String userName);
}