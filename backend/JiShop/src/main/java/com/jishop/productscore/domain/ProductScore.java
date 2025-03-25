package com.jishop.productscore.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.product.domain.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "product_scores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductScore extends BaseEntity {

    @JoinColumn(name = "product_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    @Min(0)
    @Column(nullable = false, columnDefinition = "int default 0")
    private int recentOrderCount;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(nullable = false,
            precision = 2, scale = 1,
            columnDefinition = "decimal(2,1) default 0.0")
    private BigDecimal reviewRating = BigDecimal.ZERO;

    @Min(0)
    @Column(nullable = false)
    private int reviewCount;

    @Min(0)
    @Column(nullable = false)
    private int totalOrderCount;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    @Column(nullable = false,
            precision = 3, scale = 1,
            columnDefinition = "decimal(3,1) default 0.0")
    private BigDecimal recentOrderScore = BigDecimal.ZERO;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    @Column(nullable = false,
            precision = 3, scale = 1,
            columnDefinition = "decimal(3,1) default 0.0")
    private BigDecimal reviewRatingScore = BigDecimal.ZERO;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    @Column(nullable = false,
            precision = 3, scale = 1,
            columnDefinition = "decimal(3,1) default 0.0")
    private BigDecimal reviewCountScore = BigDecimal.ZERO;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    @Column(nullable = false,
            precision = 3, scale = 1,
            columnDefinition = "decimal(3,1) default 0.0")
    private BigDecimal totalOrderScore = BigDecimal.ZERO;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "10.0")
    @Column(nullable = false,
            precision = 3, scale = 1,
            columnDefinition = "decimal(3,1) default 0.0")
    private BigDecimal weightedScore = BigDecimal.ZERO;

    @Builder
    private ProductScore(Product product,
                         int recentOrderCount,
                         BigDecimal reviewRating,
                         int reviewCount,
                         int totalOrderCount,
                         BigDecimal recentOrderScore,
                         BigDecimal reviewRatingScore,
                         BigDecimal reviewCountScore,
                         BigDecimal totalOrderScore,
                         BigDecimal weightedScore) {
        this.product = product;
        this.recentOrderCount = recentOrderCount;
        this.reviewRating = reviewRating;
        this.reviewCount = reviewCount;
        this.totalOrderCount = totalOrderCount;
        this.recentOrderScore = recentOrderScore;
        this.reviewRatingScore = reviewRatingScore;
        this.reviewCountScore = reviewCountScore;
        this.totalOrderScore = totalOrderScore;
        this.weightedScore = weightedScore;
    }

    public void setProduct(Product product) {
        this.product = product;
        if (product != null && product.getProductScore() != this) {
            product.setProductScore(this);
        }
    }

    public void updateScores(ProductScore newScore) {
        this.recentOrderCount = newScore.getRecentOrderCount();
        this.reviewRating = newScore.getReviewRating();
        this.reviewCount = newScore.getReviewCount();
        this.totalOrderCount = newScore.getTotalOrderCount();
        this.recentOrderScore = newScore.getRecentOrderScore();
        this.reviewRatingScore = newScore.getReviewRatingScore();
        this.reviewCountScore = newScore.getReviewCountScore();
        this.totalOrderScore = newScore.getTotalOrderScore();
        this.weightedScore = newScore.getWeightedScore();
    }
}
