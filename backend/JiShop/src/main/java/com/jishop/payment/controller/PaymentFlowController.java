package com.jishop.payment.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface PaymentFlowController {

    String handleSuccess(String paymentKey, String orderId, int amount, RedirectAttributes redirectAttributes);
    String handleFail(String code, String message, RedirectAttributes redirectAttributes);
    String resultPage();
}
