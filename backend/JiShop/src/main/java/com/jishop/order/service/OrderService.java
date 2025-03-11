package com.jishop.order.service;

import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse getOrder(Long orderId);
    List<OrderResponse> getAllOrders();
    void cancelOrder(Long orderId);
}
