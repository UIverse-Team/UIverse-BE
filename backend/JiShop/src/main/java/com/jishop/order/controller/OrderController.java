package com.jishop.order.controller;

import com.jishop.order.dto.OrderRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderController {
    ResponseEntity<String> create(@RequestBody OrderRequest orderRequest);
    ResponseEntity<String> getOrder(@PathVariable Long orderId);
    ResponseEntity<String> getOrderList();
    ResponseEntity<String> cancelOrder(@PathVariable Long orderId);
}
