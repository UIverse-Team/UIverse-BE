package com.jishop.payment.controller;

import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;
import com.jishop.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentFlowControllerImpl {

    private final PaymentService paymentService;

    @GetMapping("/success")
    public ResponseEntity<PaymentConfirmResponse> confirm(@Valid PaymentConfirmRequest request) {
        PaymentConfirmResponse response = paymentService.confirmPayment(request);
        // 프론트 주소로(결제 완료되었습니다 페이지) 변경, redirect + response 같이 보내기
        // Get으로 전송
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> fail(@RequestParam String code,
                                       @RequestParam String message) {
        return ResponseEntity.badRequest().body("결제 실패: " + code + " / " + message);
    }
}

