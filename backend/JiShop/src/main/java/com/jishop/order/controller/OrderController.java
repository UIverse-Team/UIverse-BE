package com.jishop.order.controller;

import com.jishop.member.domain.User;
import com.jishop.order.dto.OrderRequest;
import org.springframework.http.ResponseEntity;

public interface OrderController {

    ResponseEntity<?> create(User user, OrderRequest orderRequest);
    ResponseEntity<?> getOrder(User user, Long orderId);
    ResponseEntity<?> getOrderList(User user, String period);
    ResponseEntity<?> cancelOrder(User user, Long orderId);
}
