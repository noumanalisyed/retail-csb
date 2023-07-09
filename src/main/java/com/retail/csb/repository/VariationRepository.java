package com.retail.csb.repository;

import com.retail.csb.model.product.Variation;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface VariationRepository extends PagingAndSortingRepository<Variation, Integer> {

}
