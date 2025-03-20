package com.jishop.order.controller;

import com.jishop.address.dto.AddressResponse;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "상품 주문 조회 API")
public interface OrderController {

    ResponseEntity<OrderResponse> create(User user, OrderRequest orderRequest);
    ResponseEntity<OrderDetailPageResponse> getOrder(User user, Long orderId);
    //페이징 처리
    ResponseEntity<Page<OrderResponse>> getOrderList(User user, String period, int page, int size);
    ResponseEntity<String> cancelOrder(User user, Long orderId);
    ResponseEntity<OrderResponse> createInstantOrder(User user, InstantOrderRequest orderRequest);
    //비회원 주문
    ResponseEntity<OrderResponse> guestCreateOrder(OrderRequest orderRequest);
    ResponseEntity<OrderDetailPageResponse> getOrderDetail(String orderNumber, String phone);
    //비회원 바로 주문
    ResponseEntity<OrderResponse> guestCreateInstantOrder(InstantOrderRequest orderRequest);
    ResponseEntity<String> cancelGuestOrder(String orderNumber, String phone);
}
