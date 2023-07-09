package com.retail.csb.repository;

import com.retail.csb.model.User;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    public User findByUsername(final String username);

}
