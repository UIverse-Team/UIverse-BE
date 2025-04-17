package com.jishop.order.controller;

import com.jishop.cart.dto.CartResponse;
import com.jishop.order.dto.*;
import com.jishop.order.service.OrderCancelService;
import com.jishop.order.service.OrderCreationService;
import com.jishop.order.service.OrderGetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ordersGuest")
public class OrderGuestControllerImpl implements OrderGuestController {

    private final OrderCreationService orderCreationService;
    private final OrderGetService orderGetService;
    private final OrderCancelService orderCancelService;

    //비회원 주문 생성
    @Override
    @PostMapping
    public ResponseEntity<OrderResponse> createGuestOrder(@RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse orderResponse = orderCreationService.createOrder(orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //비회원 바로주문 생성
    @Override
    @PostMapping("/instant")
    public ResponseEntity<OrderResponse> createGuestInstantOrder(@RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse orderResponse = orderCreationService.createInstantOrder(orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //비회원 주문 조회하기
    @Override
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDetailPageResponse> getGuestOrderDetail(@PathVariable String orderNumber,
                                                                       @RequestParam String phone) {
        OrderDetailPageResponse orderDetailList = orderGetService.getOrder(orderNumber, phone);

        return ResponseEntity.ok(orderDetailList);
    }

    //비회원 주문 취소하기
    @Override
    @PatchMapping("/{orderNumber}")
    public ResponseEntity<String> cancelGuestOrder(@PathVariable String orderNumber,
                                                   @RequestParam String phone) {
        orderCancelService.cancelOrder(orderNumber, phone);

        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    //비회원 주문 취소 상세 페이지
    @Override
    @GetMapping("/getCancel/{orderNumber}")
    public ResponseEntity<OrderCancelResponse> getGuestOrderCancel(@PathVariable String orderNumber, @RequestParam String phone){
        OrderCancelResponse orderCancelResponse = orderGetService.getCancelPage(orderNumber, phone);

        return ResponseEntity.ok(orderCancelResponse);
    }

    //비회원 주문서로 넘어가는 API
    @Override
    @PostMapping("/checkout")
    public ResponseEntity<CartResponse> getGuestCheckout(@RequestBody List<OrderDetailRequest> orderDetailRequest) {
        CartResponse products = orderGetService.getCheckout(orderDetailRequest);
        return ResponseEntity.ok(products);
    }
}
