package com.jishop.order.service;

import com.jishop.order.dto.*;

public interface OrderGuestService {

    // 비회원 주문 생성
    OrderResponse createGuestOrder(OrderRequest guestOrderRequest);

    // 비회원 바로 구매하기
    OrderResponse createGuestInstantOrder(InstantOrderRequest guestInstantOrderRequest);

    // 비회원 주문 조회
    OrderDetailPageResponse getGuestOrder(String orderNumber, String phone);

    // 비회원 주문 취소
    void cancelGuestOrder(String orderNumber, String phone);

    // 비회원 주문 취소 상세 페이지
    OrderCancelResponse getGuestCancelPage(String orderNumber, String phone);
}
