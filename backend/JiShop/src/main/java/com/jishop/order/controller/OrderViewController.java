package com.jishop.order.controller;

import com.jishop.config.TossPaymentConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderViewController {

    private final TossPaymentConfig tossPaymentConfig;

    @GetMapping("/test")
    public String test() {
        return "test-order";
    }

    @GetMapping("/checkout/view")
    public String createOrderAndRedirect(@RequestParam String orderNumber,
                                         @RequestParam int amount,
                                         Model model) {
        // 결제 페이지에 필요한 데이터 전달
        model.addAttribute("orderId", orderNumber); // Toss 결제용
        model.addAttribute("amount", amount);
        model.addAttribute("clientKey", tossPaymentConfig.getClientKey());
        model.addAttribute("successUrl", "/payments/success");
        model.addAttribute("failUrl", "/payments/fail");

        return "checkout"; // templates/checkout.html
    }
}

