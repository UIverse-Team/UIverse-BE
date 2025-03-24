package com.jishop.payment.controller;


import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;
import com.jishop.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentControllerImpl implements PaymentController{

    private final PaymentService paymentService;

    /**
     * 결제 승인 요청 컨트롤러
     * 
     * @param request   토스에서 발급한 paymentKey, orderId, amount
     * @return
     */
    @Override
    @PostMapping("/confirm")
    public PaymentConfirmResponse confirmPayment(@RequestBody @Valid PaymentConfirmRequest request) {
        return paymentService.confirmPayment(request);
    }
}
