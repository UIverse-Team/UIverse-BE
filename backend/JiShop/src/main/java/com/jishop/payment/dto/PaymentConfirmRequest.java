package com.jishop.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentConfirmRequest(
        @NotBlank(message = "paymentKey는 필수입니다.")
        String paymentKey,

        // orderId는 주문번호(orderNumber)와 일치해야함
        @NotBlank(message = "orderId는 필수입니다.")
        String orderId,

        @Positive(message = "amount는 1원 이상이어야 합니다.")
        int amount
) {
}
