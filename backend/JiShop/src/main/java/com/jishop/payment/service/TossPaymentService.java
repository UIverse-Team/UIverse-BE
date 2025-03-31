package com.jishop.payment.service;

import com.jishop.order.domain.Order;
import com.jishop.payment.domain.Payment;
import com.jishop.payment.dto.TossConfirmRequest;

public interface TossPaymentService {

    Payment confirmPayment(TossConfirmRequest request, Order order);
}
