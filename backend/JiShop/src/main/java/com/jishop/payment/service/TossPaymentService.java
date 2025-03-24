package com.jishop.payment.service;

import com.jishop.payment.domain.Payment;
import com.jishop.payment.dto.PaymentResponse;
import com.jishop.payment.dto.TossConfirmRequest;

public interface TossPaymentService {

    Payment confirmPayment(TossConfirmRequest request);
}
