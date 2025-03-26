package com.jishop.reviewproduct.repository;

import com.jishop.product.domain.Product;
import com.jishop.reviewproduct.domain.ReviewProduct;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ReviewProductRepository extends JpaRepository<ReviewProduct, Long> {

    Optional<ReviewProduct> findByProduct(Product product);

    @Modifying
    @Query("UPDATE ReviewProduct rp SET rp.reviewScore = rp.reviewScore - :rating, rp.reviewCount = rp.reviewCount - 1 " +
            "WHERE rp.product = :product")
    void decreaseRatingAtomic(@Param("product") Product product, @Param("rating") int rating);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select rp from ReviewProduct rp where rp.product = :product")
    Optional<ReviewProduct> findByProductForUpdate(@Param("product") Product product);

    /**
     * 특정 상품의 리뷰 평점 조회
     * 리뷰가 없는 경우 0 반환
     *
     * @param productId     조회할 상품 id
     * @return 해당 상품의 리뷰 평점
     */
    @Query("SELECT CASE WHEN rp.reviewCount > 0 " +
            "THEN CAST(rp.reviewScore as double) / rp.reviewCount " +
            "ELSE 0 END " +
            "FROM ReviewProduct rp " +
            "WHERE rp.product.id = :productId")
    BigDecimal getReviewRatingByProductId(@Param("productId") Long productId);

    /**
     * 특정 상품의 리뷰 수 조회
     *
     * @param productId     조회할 상품id
     * @return 해당 상품의 리뷰수
     */
    @Query("SELECT COALESCE(rp.reviewCount, 0) FROM ReviewProduct rp " +
            "WHERE rp.product.id = :productId")
    Integer countReviewsByProductId(@Param("productId") Long productId);
}
