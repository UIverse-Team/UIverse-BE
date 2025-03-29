package com.jishop.order.controller;

import com.jishop.cart.dto.CartResponse;
import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "주문 API", description = "주문 관련 API")
public interface OrderController {

    //회원 주문
    @Operation(summary = "회원 주문", description = "회원이 장바구니에서 주문 시 사용하는 API")
    ResponseEntity<OrderResponse> createOrder(User user, OrderRequest orderRequest);

    //회원 바로주문
    @Operation(summary = "회원 바로 주문", description = "회원이 상품 상세 페이지에서 바로 주문할 때 사용되는 API")
    ResponseEntity<OrderResponse> createInstantOrder(User user, InstantOrderRequest orderRequest);

    //회원 주문 목록 조회 페이징 처리
    @Operation(summary = "회원 주문 목록 조회", description = "조회하는 회원의 ID에 따른 주문 목록 페이지")
    ResponseEntity<Page<OrderResponse>> getOrderList(
            User user,
            @Parameter(description = "조회하는 기간 설정" ,example = "all, 1month, 6months") String period,
            @Parameter(description = "페이지 번호를 위한 값", example = "0") int page,
            @Parameter(description = "각 페이지에 가져올 데이터의 개수를 정하는 값", example = "10") int size
    );

    //회원 주문 상세 조회
    @Operation(summary = "회원 주문 상세 조회", description = "orderId로 조회 가능")
    ResponseEntity<OrderDetailPageResponse> getOrder(
            User user,
            @Parameter(description = "조회할 주문 ID", example = "1") Long orderId
    );

    //회원 주문 취소
    @Operation(summary = "회원 주문 취소", description = "orderId로 주문 취소 가능")
    ResponseEntity<String> cancelOrder(
            User user,
            @Parameter(description = "조회할 주문 ID", example = "1") Long orderId
    );

    //회원 취소 상세페이지
    @Operation(summary = "회원 취소 상세 페이지", description = "orderId로 취소 상세 페이지 조회 가능")
    ResponseEntity<OrderCancelResponse> getOrderCancel(
            User user,
            @Parameter(description = "조회할 주문 ID", example = "1") Long orderId
    );

    // 장바구니에서 주문서로 넘어가는 API
    @Operation(summary = "장바구니에서 주문서로 넘어갈 때 사용하는 API")
    ResponseEntity<CartResponse> getCheckout(User user, List<OrderDetailRequest> orderDetailRequest);

    //바로 주문하기에서 주문서로 넘어가는 API
    @Operation(summary = "바로 주문하기에서 주문서로 넘어갈 때 사용하는 API")
    ResponseEntity<CartResponse> getCheckoutInstant(User user, Long saleProductId, int quantity);
}
