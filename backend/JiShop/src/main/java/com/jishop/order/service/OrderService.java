package com.jishop.order.service;

import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import org.springframework.data.domain.Page;

public interface OrderService {

    //회원
    OrderResponse createOrder(User user, OrderRequest orderRequest);
    OrderResponse createInstantOrder(User user, InstantOrderRequest instantOrderRequest);
    OrderDetailPageResponse getOrder(User user, Long orderId, String orderNumber, String phone);
    Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size);
    void cancelOrder(User user, Long orderId, String orderNumber, String phone);
    OrderCancelResponse getCancelPage(User user, Long orderId, String orderNumber, String phone);

    //비회원
    OrderResponse createGuestOrder(OrderRequest orderRequest);
    OrderResponse createGuestInstantOrder(InstantOrderRequest orderRequest);
    OrderDetailPageResponse getGuestOrder(String orderNumber, String phone);
    void cancelGuestOrder(String orderNumber, String phone);
    OrderCancelResponse getGuestCancelPage(String orderNumber, String phone);
}
