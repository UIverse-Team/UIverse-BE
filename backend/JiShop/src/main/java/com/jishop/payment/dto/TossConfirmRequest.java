package com.jishop.payment.dto;

import com.jishop.payment.domain.Payment;

public record TossConfirmRequest(
        String paymentKey,
        String orderId,
        int amount
) {
    public static TossConfirmRequest fromPaymentConfirmRequest(PaymentConfirmRequest request) {
        return new TossConfirmRequest(
                request.paymentKey(),
                request.orderId(),
                request.amount()
        );
    }
}
