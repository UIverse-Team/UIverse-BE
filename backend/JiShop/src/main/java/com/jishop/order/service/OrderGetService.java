package com.jishop.order.service;

import com.jishop.cart.dto.CartResponse;
import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderGetService {
    //회원 주문 상세 페이지
    OrderDetailPageResponse getOrder(User user, Long orderId);
    //회원 주문 취소 상세 페이지
    OrderCancelResponse getCancelPage(User user, Long orderId);
    //회원 페이징 처리 주문 목록 전체 조회
    Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size);
    //회원 장바구니에서 주문서로 넘어가는 API
    CartResponse getCheckout(User user, List<OrderDetailRequest> orderDetailRequest);
    //리뷰 작성 시 필요한 상품 정보 내려주는 API
    OrderProductResponse getItem(Long orderDetailId);

    //비회원 주문 상세 페이지
    OrderDetailPageResponse getOrder(String orderNumber, String phone);
    //비회원 주문 취소 상세 페이지
    OrderCancelResponse getCancelPage(String orderNumber, String phone);
    //비회원 장바구니에서 주문서로 넘어가는 API
    CartResponse getCheckout(List<OrderDetailRequest> orderDetailRequest);
}
