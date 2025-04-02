package com.jishop.payment.controller;

import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "결제 승인 성공 및 실패 API")
public interface PaymentFlowController {

    @Operation(
            summary = "결제 승인 성공 API",
            description = "토스페이먼츠로 보낸 결제 승인 요청 성공 시 리다렉트되는 API"
    )
    ResponseEntity<PaymentConfirmResponse> confirm(PaymentConfirmRequest request);


    @Operation(
            summary = "결제 승인 실패 API",
            description = "토스페이먼츠로 보낸 결제 승인 요청 실패 시 리다렉트되는 API"
    )
    ResponseEntity<String> fail(String code, String message);
}
