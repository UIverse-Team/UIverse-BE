package com.jishop.product.domain.embed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInfo {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "mall_seq", nullable = false)
    private String mallSeq;
    @Column(name = "manufacture_date", nullable = false)
    private LocalDateTime manufactureDate;
    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "origin_price", nullable = false)
    private int originPrice;
    @Column(name = "discount_price", nullable = false)
    private int discountPrice;
    @Column(name = "discount_rate", nullable = false)
    private int discountRate;

    public ProductInfo(String name, String mallSeq, LocalDateTime manufactureDate,
            String brand, String description, int originPrice, int discountPrice, int discountRate
    ) {
        this.name = name;
        this.mallSeq = mallSeq;
        this.manufactureDate = manufactureDate;
        this.brand = brand;
        this.description = description;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
    }
}
