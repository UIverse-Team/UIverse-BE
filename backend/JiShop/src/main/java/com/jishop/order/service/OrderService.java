package com.jishop.order.service;

import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import org.springframework.data.domain.Page;

public interface OrderService {

    //회원 비회원 주문 생성
    OrderResponse createOrder(User user, OrderRequest orderRequest);

    //회원 비회원 바로주문
    OrderResponse createInstantOrder(User user, InstantOrderRequest orderRequest);

    //회원 비회원 주문 상세 페이지
    OrderDetailPageResponse getOrder(User user, Long orderId, String orderNumber, String phone);

    //페이징 처리 주문 목록 전체 조회
    Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size);

    //회원 비회원 주문 취소
    void cancelOrder(User user, Long orderId, String orderNumber, String phone);

    //회원 비회원 주문 상세 페이지
    OrderCancelResponse getCancelPage(User user, Long orderId);
}
