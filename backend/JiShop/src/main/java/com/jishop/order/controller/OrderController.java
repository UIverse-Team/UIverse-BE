package com.jishop.order.controller;

import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OrderController {

    ResponseEntity<OrderResponse> create(OrderRequest orderRequest);
    ResponseEntity<OrderResponse> getOrder(Long orderId);
    ResponseEntity<List<OrderResponse>> getOrderList();
    ResponseEntity<String> cancelOrder(Long orderId);
}
