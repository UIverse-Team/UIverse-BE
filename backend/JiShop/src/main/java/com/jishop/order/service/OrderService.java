package com.jishop.order.service;

import com.jishop.member.domain.User;
import com.jishop.order.dto.OrderDetailResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(User user, OrderRequest orderRequest);
    List<OrderDetailResponse> getOrder(User user, Long orderId);
    List<OrderResponse> getAllOrders(User user, String period);
    void cancelOrder(User user, Long orderId);
}
