package com.jishop.order.controller;

import com.jishop.order.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "비회원 주문 API")
public interface OrderGuestController {

    //비회원 주문 생성
    @Operation(summary = "비회원 주문", description = "비회원 장바구니에서 주문 시 사용하는 API, userId를 null로 처리하여 주문 진행")
    ResponseEntity<OrderResponse> createGuestOrder(OrderRequest orderRequest);

    //비회원 바로 주문 생성
    @Operation(summary = "비회원 바로 주문", description = "비회원 바로 주문 시 userId를 null로 처리하여 주문 진행")
    ResponseEntity<OrderResponse> createGuestInstantOrder(OrderRequest orderRequest);

    //비회원 주문 상세 조회
    @Operation(summary = "비회원 상세 조회", description = "비회원은 주문 목록 조회가 존재하지 않고 주문 번호와 주문 시 사용했던 전화번호로 조회가 가능")
    ResponseEntity<OrderDetailPageResponse> getGuestOrderDetail(
            @Parameter(description = "조회할 주문번호", example = "O250327QNPUY") String orderNumber,
            @Parameter(description = "조회할 주문 수신자의 전화번호", example = "01012345678") String phone
    );

    //비회원 주문 취소
    @Operation(summary = "비회원 주문 취소", description = "비회원은 주문 번호와 주문 시 사용했던 전화번호로 취소가 가능")
    ResponseEntity<String> cancelGuestOrder(
            @Parameter(description = "조회할 주문번호", example = "O250327QNPUY") String orderNumber,
            @Parameter(description = "조회할 주문 수신자의 전화번호", example = "01012345678") String phone
    );

    //비회원 취소 상세페이지
    @Operation(summary = "비회원 주문 취소 상세 페이지", description = "비회원은 주문 번호와 주문 시 사용했던 전화번호로 주문 취소 상세 페이지 조회가 가능")
    ResponseEntity<OrderCancelResponse> getGuestOrderCancel(
            @Parameter(description = "조회할 주문번호", example = "O250327QNPUY") String orderNumber,
            @Parameter(description = "조회할 주문 수신자의 전화번호", example = "01012345678") String phone
    );
}
