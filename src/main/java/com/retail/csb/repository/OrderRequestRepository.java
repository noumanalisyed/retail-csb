package com.retail.csb.repository;

import com.retail.csb.model.cart.OrderRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRequestRepository extends PagingAndSortingRepository<OrderRequest, Long> {
    Page<OrderRequest> findAllByCustomerId(final Long customerId, final Pageable page);
}
