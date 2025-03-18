package com.jishop.order.service;

import com.jishop.address.domain.Address;
import com.jishop.address.dto.AddressResponse;
import com.jishop.member.domain.User;
import com.jishop.order.dto.InstantOrderRequest;
import com.jishop.order.dto.OrderDetailResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(User user, OrderRequest orderRequest);
    List<OrderDetailResponse> getOrder(User user, Long orderId);
    //페이징 처리 주문 목록 전체 조회
    Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size);
    void cancelOrder(User user, Long orderId);
    OrderResponse createInstantOrder(User user, InstantOrderRequest orderRequest);
    ResponseEntity<OrderResponse> createGuestOrder(OrderRequest orderRequest);
    List<OrderDetailResponse> getGuestOrder(String orderNumber, String phoneNumber);
    ResponseEntity<OrderResponse> createGuestInstantOrder(InstantOrderRequest orderRequest);
    void cancelGuestOrder(String orderNumber, String phoneNumber);
}
