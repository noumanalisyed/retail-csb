package com.retail.csb.repository;

import com.retail.csb.model.product.Brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {

    @Transactional(timeout = 10)
    @Query(value = "SELECT * FROM brands ORDER BY name ASC", nativeQuery = true)
    public Page<Brand> findAllByBusinessId(final Integer businessId, final Pageable page);

    public Brand findByIdAndBusinessId(final Integer id, final Integer businessId);

}
