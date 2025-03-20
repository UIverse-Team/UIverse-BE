package com.jishop.review.repository;

import com.jishop.member.domain.User;
import com.jishop.review.domain.Review;
import jakarta.persistence.LockModeType;
import kotlin.OptIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByOrderDetailId(Long orderDetailId);

    @Query("SELECT r.orderDetail.id FROM Review r WHERE r.orderDetail.id IN :orderDetailIds")
    List<Long> findOrderDetailIdsWithReviews(List<Long> orderDetailIds);


    @Query("select r from Review r join fetch r.user where r.product.id = :id and r.deleteStatus = false")
    Page<Review> findByProductIdWithUser(@Param("id") Long productId, Pageable pageable);

    @Query("select r from Review r join fetch r.product where r.user.id = :userId and r.deleteStatus = false")
    Page<Review> findReviewsProductByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select r, case when lr.id is not null then true else false end isLike " +
            "from Review r " +
            "join fetch r.user u " +
            "left join LikeReview lr on r.id = lr.review.id and lr.user.id = :userId " +
            "where r.product.id = :productId and r.deleteStatus = false")
    Page<Object[]> findReviewsWithUserLike(@Param("productId") Long productId, @Param("userId") Long userId, Pageable pageable);

    @Query("select r from Review r where r.id = :reviewId and r.user.id = :userId")
    Optional<Review> findByReviewIdAndUserId(@Param("reviewId") Long reviewId, @Param("userId") Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Review r join fetch r.user u join fetch r.product p where r.id = :id")
    Optional<Review> findByIdWithLock(@Param("id") Long reviewId);
    /**
     * 사용 보류
     */
    @Query("select r from Review r join fetch r.user where r.id = :reviewId")
    Optional<Review> findByReviewIdWithUser(@Param("reviewId") Long reviewId);
    @Query("update Review r set r.deleteStatus = true where r.id = :id and r.user.id = :userId and r.deleteStatus = false")
    int deleteReviewAtomic(@Param("id") Long reviewId, @Param("userId") Long userId);

}
