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
    private String provider;        // 간편 결제 제공자 - 토스페이

    @Column(name = "easypay_amount")
    private int amount;             // 간편 결제 금액
    private int discountAmount;     // 할인 금액

    @Builder
    private EasyPayInfo(String provider, int amount, int discountAmount) {
        this.provider = provider;
        this.amount = amount;
        this.discountAmount = discountAmount;
    }
}
