package com.jishop.order.service;

import com.jishop.cart.dto.CartResponse;
import com.jishop.member.domain.User;
import com.jishop.order.dto.OrderCancelResponse;
import com.jishop.order.dto.OrderDetailPageResponse;
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderGetService {
    //회원 비회원 주문 상세 페이지
    OrderDetailPageResponse getOrder(User user, Long orderId, String orderNumber, String phone);

    //페이징 처리 주문 목록 전체 조회
    Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size);

    //회원 비회원 주문취소  상세 페이지
    OrderCancelResponse getCancelPage(User user, Long orderId, String orderNumber, String phone);

    //회원 비회원 장바구니에서 주문서로 넘어가는 API
    CartResponse getCheckout(User user, List<OrderDetailRequest> orderDetailRequest);
}
