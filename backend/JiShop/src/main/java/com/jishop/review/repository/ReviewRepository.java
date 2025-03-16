package com.jishop.review.repository;

import com.jishop.review.domain.Review;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByOrderDetailId(Long orderDetailId);

    @Query("SELECT r.orderDetail.id FROM Review r WHERE r.orderDetail.id IN :orderDetailIds")
    List<Long> findOrderDetailIdsWithReviews(List<Long> orderDetailIds);
}
