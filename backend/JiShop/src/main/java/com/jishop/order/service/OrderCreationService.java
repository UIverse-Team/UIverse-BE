package com.jishop.order.service;

import com.jishop.member.domain.User;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;

public interface OrderCreationService {
    //회원 주문 생성
    OrderResponse createOrder(User user, OrderRequest orderRequest);
    //비회원 주문 생성
    OrderResponse createOrder(OrderRequest orderRequest);
}
