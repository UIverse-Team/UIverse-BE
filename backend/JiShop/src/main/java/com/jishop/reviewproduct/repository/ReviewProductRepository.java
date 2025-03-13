package com.jishop.reviewproduct.repository;

import com.jishop.product.domain.Product;
import com.jishop.reviewproduct.domain.ReviewProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewProductRepository extends JpaRepository<ReviewProduct, Long> {

    Optional<ReviewProduct> findByProduct(Product product);
}
