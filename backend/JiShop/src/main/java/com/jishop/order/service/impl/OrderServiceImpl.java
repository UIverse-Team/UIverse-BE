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

    //회원 주문 생성
    @Override
    public OrderResponse createOrder(User user, OrderRequest orderRequest) {
        return orderCreationService.createOrder(user, orderRequest);
    }

    //회원 바로 주문 생성
    @Override
    public OrderResponse createInstantOrder(User user, InstantOrderRequest instantOrderRequest) {
        return orderCreationService.createInstantOrder(user, instantOrderRequest);
    }

    //회원 주문 상세 조회
    @Override
    public OrderDetailPageResponse getOrder(User user, Long orderId, String orderNumber, String phone) {
        return orderGetService.getOrder(user, orderId, orderNumber, phone);
    }

    //회원 주문 목록 조회
    @Override
    public Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size) {
        return orderGetService.getPaginatedOrders(user, period, page, size);
    }

    //회원, 비회원 주문 취소
    @Override
    public void cancelOrder(User user, Long orderId, String orderNumber, String phone) {
        orderCancelService.cancelOrder(user, orderId, orderNumber, phone);
    }

    //회원, 비회원 주문 취소 상세 페이지 조회
    @Override
    public OrderCancelResponse getCancelPage(User user, Long orderId, String orderNumber, String phone) {
        return orderGetService.getCancelPage(user, orderId, orderNumber, phone);
    }

    //비회원 주문 생성
    @Override
    public OrderResponse createGuestOrder(OrderRequest orderRequest) {
        return orderCreationService.createOrder(null, orderRequest);
    }

    //비회원 바로 주문 생성
    @Override
    public OrderResponse createGuestInstantOrder(InstantOrderRequest orderRequest) {
        return orderCreationService.createInstantOrder(null, orderRequest);
    }

    //비회원 주문 상세 페이지
    @Override
    public OrderDetailPageResponse getGuestOrder(String orderNumber, String phone) {
        return orderGetService.getOrder(null, null, orderNumber, phone);
    }

    //비회원 주문 취소
    @Override
    public void cancelGuestOrder(String orderNumber, String phone) {
        orderCancelService.cancelOrder(null, null, orderNumber, phone);
    }

    //비회원 주문 취소 페이지 조회
    @Override
    public OrderCancelResponse getGuestCancelPage(String orderNumber, String phone) {
        return orderGetService.getCancelPage(null, null, orderNumber, phone);
    }
}