package com.jishop.order.controller;

import com.jishop.order.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "비회원 주문 API")
public interface OrderGuestController {

    //비회원 주문 생성
    ResponseEntity<OrderResponse> createGuestOrder(OrderRequest orderRequest);

    //비회원 바로 주문 생성
    ResponseEntity<OrderResponse> createGuestInstantOrder(InstantOrderRequest orderRequest);

    //비회원 주문 상세 조회
    ResponseEntity<OrderDetailPageResponse> getGuestOrderDetail(String orderNumber, String phone);

    //비회원 주문 취소
    ResponseEntity<String> cancelGuestOrder(String orderNumber, String phone);

    //비회원 취소 상세페이지
    ResponseEntity<OrderCancelResponse> getGuestOrderCancel(String orderNumber, String phone);
}
