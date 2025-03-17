package com.jishop.order.controller;

import com.jishop.address.dto.AddressResponse;
import com.jishop.member.domain.User;
import com.jishop.order.dto.InstantOrderRequest;
import com.jishop.order.dto.OrderDetailResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
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
}
