package com.retail.csb.repository;

import com.retail.csb.model.product.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
    List<ProductReview> findByProducts_Id(Long id);

}

