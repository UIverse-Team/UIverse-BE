package com.jishop.reviewproduct.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.product.domain.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "review_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewProduct extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    private int reviewCount;

    private int reviewScore;

    @Builder
    public ReviewProduct(Product product, int reviewCount, int reviewScore) {
        this.product = product;
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
    }

    public void increaseRating(int rating) {
        reviewCount++;
        reviewScore += rating;
    }

    public void decreaseRating(int rating) {
        reviewCount--;
        reviewScore -= rating;

    }
}
