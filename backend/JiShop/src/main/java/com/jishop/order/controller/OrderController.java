package com.jishop.order.controller;

import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "상품 주문 조회 API")
public interface OrderController {

    //회원, 비회원 주문
    ResponseEntity<OrderResponse> createOrder(User user, OrderRequest orderRequest);

    //회원 주문 상세 조회
    ResponseEntity<OrderDetailPageResponse> getOrder(User user, Long orderId);

    //회원 주문 목록 조회 페이징 처리
    ResponseEntity<Page<OrderResponse>> getOrderList(User user, String period, int page, int size);

    //회원 주문 취소
    ResponseEntity<String> cancelOrder(User user, Long orderId);

    //회원, 비회원 바로 주문
    ResponseEntity<OrderResponse> createInstantOrder(User user, InstantOrderRequest orderRequest);

    //비회원 주문 상세 조회
    ResponseEntity<OrderDetailPageResponse> getGuestOrderDetail(String orderNumber, String phone);

    //비회원 주문 취소
    ResponseEntity<String> cancelGuestOrder(String orderNumber, String phone);

    //취소 상세페이지
    ResponseEntity<OrderCancelResponse> getOrderCancel(User user, Long orderId);
}
