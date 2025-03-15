package com.jishop.order.controller;

import com.jishop.member.domain.User;
import com.jishop.order.dto.OrderDetailResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderController {

    ResponseEntity<OrderResponse> create(User user, OrderRequest orderRequest);
    ResponseEntity<List<OrderDetailResponse>> getOrder(User user, Long orderId);
    ResponseEntity<List<OrderResponse>> getOrderList(User user, String period);
    ResponseEntity<String> cancelOrder(User user, Long orderId);
}
