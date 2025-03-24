package com.jishop.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EasyPayInfo {

    // TODO: 토스페이 제공자 확인
        // 토스페이, 카카오페이, 네이퍼페이
    private PaymentProvider provider;

    @Column(name = "easypay_amount")
    private int amount;             // 간편 결제 금액
    private int discountAmount;     // 할인 금액

    @Builder
    private EasyPayInfo(PaymentProvider provider, int amount, int discountAmount) {
        this.provider = provider;
        this.amount = amount;
        this.discountAmount = discountAmount;
    }
}
