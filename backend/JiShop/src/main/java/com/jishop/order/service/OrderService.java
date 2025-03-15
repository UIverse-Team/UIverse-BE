package com.jishop.order.service;

import com.jishop.member.domain.User;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(User user, OrderRequest orderRequest);
    OrderResponse getOrder(User user, Long orderId);
    List<OrderResponse> getAllOrders(User user);
    void cancelOrder(User user, Long orderId);
}
