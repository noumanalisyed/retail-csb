package com.retail.csb.repository;

import com.retail.csb.model.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

    @Transactional(timeout = 10)
    public Page<Category> findAllByBusinessId(final Integer businessId, final Pageable page);

    public Category findByIdAndBusinessId(final Integer id, final Integer businessId);

}
