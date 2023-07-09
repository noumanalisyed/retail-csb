package com.retail.csb.repository;

import com.retail.csb.model.product.Product;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Transactional(timeout = 10)
    public Page<Product> findAllByBusinessId(final Integer businessId, final Pageable page);

    /**
     * Query to get the exact or partial match(search) the products Refer to
     * {@link org.springframework.data.jpa.repository.Query}
     *
     * @param businessId Business Id to search in a specific business brands
     * @param brand      Brand name or Brand search query
     * @return List of Products
     */
    @Transactional(timeout = 10)
    @Query(value = "SELECT * FROM products p LEFT JOIN brands b ON p.brand_id=b.id"
            + " WHERE b.business_id=:businessId AND (b.name=:brand OR b.name LIKE :brand%)", nativeQuery = true)
    public Page<Product> findAllByBrandName(@Param("businessId") final String businessId,
            @Param("brand") final String brand, final Pageable page);

    public Page<Product> findByNameAllIgnoringCaseContainingAndBusinessId(String query, Integer businessId,
            Pageable page);

    @Async
    CompletableFuture<List<Product>> findFirst25ByNameAllIgnoringCaseContainingAndBusinessId(String query, Integer businessId);

    public Product findById(Long id);
}
