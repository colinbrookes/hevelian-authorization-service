package com.hevelian.identity.users.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.hevelian.identity.users.model.PrimaryUser;

public interface PrimaryUserRepository extends PagingAndSortingRepository<PrimaryUser, String> {
    PrimaryUser findOneByName(String userName);

    void deleteByName(String userName);
}