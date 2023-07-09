package com.retail.csb.repository;

import com.retail.csb.model.Business;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BusinessRepository extends PagingAndSortingRepository<Business, Integer> {

    public Business findByApiKey(final String apiKey);

}
