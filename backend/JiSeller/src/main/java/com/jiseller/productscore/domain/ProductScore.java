package com.jiseller.productscore.domain;

import com.jiseller.common.util.BaseEntity;
import com.jiseller.product.domain.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductScore extends BaseEntity {

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

    @JoinColumn(name = "product_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Product product;
}
