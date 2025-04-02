package com.jishop.payment.service;

import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;

public interface PaymentService {

    PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request);
}
