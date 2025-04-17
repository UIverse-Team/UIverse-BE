package com.jishop.payment.controller;

import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "결제 API")
public interface PaymentController {

    @Operation(
            summary = "결제 승인 요청 API",
            description = "토스페이먼츠로 결제 승인 요청을 보내는 API"
    )
    PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request);
}
