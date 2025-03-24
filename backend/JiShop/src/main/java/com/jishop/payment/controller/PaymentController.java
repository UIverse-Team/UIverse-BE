package com.jishop.payment.controller;

import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;

public interface PaymentController {

    PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request);
}
