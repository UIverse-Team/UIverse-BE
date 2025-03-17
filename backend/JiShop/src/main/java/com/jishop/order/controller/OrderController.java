package com.jishop.order.controller;

import com.jishop.address.dto.AddressResponse;
import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 주문 조회 API")
public interface OrderController {

    ResponseEntity<OrderResponse> create(User user, OrderRequest orderRequest);
    ResponseEntity<List<OrderDetailResponse>> getOrder(User user, Long orderId);
    ResponseEntity<List<OrderResponse>> getOrderList(User user, String period);
    ResponseEntity<String> cancelOrder(User user, Long orderId);
    ResponseEntity<OrderResponse> createInstantOrder(User user, InstantOrderRequest orderRequest);
    //비회원 주문
    ResponseEntity<OrderResponse> guestCreateOrder(OrderRequest orderRequest);
    //비회원 장바구니 주문
    ResponseEntity<List<OrderDetailResponse>> getOrderDetail(String orderNumber, String phone);
    //비회원 바로 주문
    ResponseEntity<OrderResponse> guestCreateInstantOrder(InstantOrderRequest orderRequest);
    ResponseEntity<String> cancelGuestOrder(String orderNumber, String phone);
}
