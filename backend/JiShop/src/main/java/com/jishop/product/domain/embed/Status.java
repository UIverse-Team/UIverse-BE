package com.jishop.product.domain.embed;

import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Labels;
import com.jishop.product.domain.SaleStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Status {

    @Column(name = "secret", nullable = false)
    private Boolean secret;
    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status", nullable = false)
    private SaleStatus saleStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "labels", length = 50)
    private Labels labels;
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_status", nullable = false)
    private DiscountStatus discountStatus;

    // "오늘의 특가" 구분 필드값
    @Column(name = "is_discount", nullable = false)
    private Boolean isDiscount;

    public Status(Boolean secret, SaleStatus saleStatus, Labels labels, Boolean isDiscount, DiscountStatus discountStatus
    ) {
        this.secret = secret;
        this.saleStatus = saleStatus;
        this.labels = labels;
        this.isDiscount = isDiscount;
        this.discountStatus = discountStatus;
    }
}
