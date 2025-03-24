package com.jishop.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jishop.payment.domain.Payment;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        String paymentKey,
        String orderId,
        String status,
        LocalDateTime requestedAt,
        LocalDateTime approvedAt,
        String method,
        String currency,
        int totalAmount,
        int suppliedAmount,
        int vat,
        int taxFreeAmount,
        CardInfoResponse card,
        EasyPayInfoResponse easyPay
) {
    public static PaymentResponse fromPayment(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentKey(),
                payment.getOrderId(),
                payment.getStatus().name(),
                payment.getRequestedAt(),
                payment.getApprovedAt(),
                payment.getMethod().name(),
                payment.getCurrency(),
                payment.getTotalAmount(),
                payment.getSuppliedAmount(),
                payment.getVat(),
                payment.getTaxFreeAmount(),
                payment.getCard() != null ? CardInfoResponse.fromCardInfo(payment.getCard()) : null,
                payment.getEasyPay() != null ? EasyPayInfoResponse.fromEasyPayInfo(payment.getEasyPay()) : null
        );
    }
}
