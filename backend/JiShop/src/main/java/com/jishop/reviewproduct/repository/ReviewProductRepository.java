package com.jishop.reviewproduct.repository;

import com.jishop.product.domain.Product;
import com.jishop.reviewproduct.domain.ReviewProduct;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewProductRepository extends JpaRepository<ReviewProduct, Long> {

    Optional<ReviewProduct> findByProduct(Product product);

    @Modifying
    @Query("UPDATE ReviewProduct rp SET rp.reviewScore = rp.reviewScore - :rating, rp.reviewCount = rp.reviewCount - 1 " +
            "WHERE rp.product = :product")
    void decreaseRatingAtomic(@Param("product") Product product, @Param("rating") int rating);

    ReviewProduct findByProductId(Long productId);
}
