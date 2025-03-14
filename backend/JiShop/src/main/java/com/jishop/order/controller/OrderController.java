package com.jishop.order.controller;

import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderController {

    ResponseEntity<OrderResponse> create(OrderRequest orderRequest);
    ResponseEntity<OrderResponse> getOrder(Long orderId);
    ResponseEntity<List<OrderResponse>> getOrderList();
    ResponseEntity<String> cancelOrder(Long orderId);
}
