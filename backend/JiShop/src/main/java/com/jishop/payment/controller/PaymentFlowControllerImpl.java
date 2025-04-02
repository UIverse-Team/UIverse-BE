package com.jishop.payment.controller;

import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;
import com.jishop.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentFlowControllerImpl {

    private final PaymentService paymentService;

//    @GetMapping("/success")
//    public ResponseEntity<PaymentConfirmResponse> confirm(@Valid PaymentConfirmRequest request) {
//        PaymentConfirmResponse response = paymentService.confirmPayment(request);
//        // 프론트 주소로(결제 완료되었습니다 페이지) 변경, redirect + response 같이 보내기
//        // Get으로 전송
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/success")
    public void confirm(@Valid PaymentConfirmRequest request, HttpServletResponse response) throws IOException {
        PaymentConfirmResponse confirmResponse = paymentService.confirmPayment(request);

        // 프론트 주소로 주문번호(orderNumber) 전달
        String redirectUrl = UriComponentsBuilder.fromUriString("https://uiverse.shop/purchase-complete")
                .queryParam("orderNumber", confirmResponse.orderId())
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> fail(@RequestParam String code,
                                       @RequestParam String message) {
        return ResponseEntity.badRequest().body("결제 실패: " + code + " / " + message);
    }
}

