package com.jishop.order.service;

import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import org.springframework.data.domain.Page;

public interface OrderService {

    OrderResponse createOrder(User user, OrderRequest orderRequest);
    OrderDetailPageResponse getOrder(User user, Long orderId);
    //페이징 처리 주문 목록 전체 조회
    Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size);
    void cancelOrder(User user, Long orderId);
    OrderResponse createInstantOrder(User user, InstantOrderRequest orderRequest);
    OrderResponse createGuestOrder(OrderRequest orderRequest);
    OrderDetailPageResponse getGuestOrder(String orderNumber, String phoneNumber);
    OrderResponse createGuestInstantOrder(InstantOrderRequest orderRequest);
    void cancelGuestOrder(String orderNumber, String phoneNumber);
    OrderCancelResponse getCancelPage(User user, Long orderId);
    OrderCancelResponse cancelGuestOrder(Long orderId);
}
