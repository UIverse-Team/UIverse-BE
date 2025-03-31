package com.jishop.payment.dto;

import com.jishop.payment.domain.Payment;

public record PaymentConfirmResponse(
        String orderId,
        int amount,
        String status,
        String message      // 결제 승인 결과에 따른 메세지
) {
    public static PaymentConfirmResponse fromPayment(Payment payment, String message) {
        return new PaymentConfirmResponse(
                payment.getOrderNumber(),
                payment.getTotalAmount(),
                payment.getStatus().name(),
                message
        );
    }
}
