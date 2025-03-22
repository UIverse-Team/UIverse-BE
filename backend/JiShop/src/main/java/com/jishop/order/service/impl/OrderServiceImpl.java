package com.jishop.order.service.impl;

import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import com.jishop.order.service.OrderCancelService;
import com.jishop.order.service.OrderCreationService;
import com.jishop.order.service.OrderGetService;
import com.jishop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderCreationService orderCreationService;
    private final OrderGetService orderGetService;
    private final OrderCancelService orderCancelService;

    @Override
    public OrderResponse createOrder(User user, OrderRequest orderRequest) {
        return orderCreationService.createOrder(user, orderRequest);
    }

    @Override
    public OrderResponse createInstantOrder(User user, InstantOrderRequest instantOrderRequest) {
        return orderCreationService.createInstantOrder(user, instantOrderRequest);
    }

    @Override
    public OrderDetailPageResponse getOrder(User user, Long orderId, String orderNumber, String phone) {
        return orderGetService.getOrder(user, orderId, orderNumber, phone);
    }

    @Override
    public Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size) {
        return orderGetService.getPaginatedOrders(user, period, page, size);
    }

    @Override
    public void cancelOrder(User user, Long orderId, String orderNumber, String phone) {
        orderCancelService.cancelOrder(user, orderId, orderNumber, phone);
    }

    @Override
    public OrderCancelResponse getCancelPage(User user, Long orderId, String orderNumber, String phone) {
        return orderGetService.getCancelPage(user, orderId, orderNumber, phone);
    }
}