package com.jishop.review.repository;

import com.jishop.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByOrderDetailId(Long orderDetailId);

    @Query("select r from Review r join fetch r.user where r.product.id = :id")
    Page<Review> findByProductIdWithUser(@Param("id") Long productId, Pageable pageable);

    @Query("select r from Review r join fetch r.product where r.user.id = :userId")
    Page<Review> findReviewsProductByUserId(@Param("userId") Long userId, Pageable pageable);
}
