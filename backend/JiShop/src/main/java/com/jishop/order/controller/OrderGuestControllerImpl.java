package com.jishop.order.controller;

import com.jishop.order.dto.*;
import com.jishop.order.service.OrderGuestService;
import com.jishop.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ordersGuest")
public class OrderGuestControllerImpl implements OrderGuestController {

    private final OrderService orderService;

    //비회원 주문 생성
    @Override
    @PostMapping
    public ResponseEntity<OrderResponse> createGuestOrder(@RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createGuestOrder(orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //비회원 바로주문 생성
    @Override
    @PostMapping("/instant")
    public ResponseEntity<OrderResponse> createGuestInstantOrder(@RequestBody @Valid InstantOrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createGuestInstantOrder(orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //비회원 주문 조회하기
    @Override
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDetailPageResponse> getGuestOrderDetail(@PathVariable String orderNumber,
                                                                       @RequestParam String phone) {
        OrderDetailPageResponse orderDetailList = orderService.getGuestOrder(orderNumber, phone);

        return ResponseEntity.ok(orderDetailList);
    }

    //비회원 주문 취소하기
//    @Override
//    @PatchMapping("/{orderNumber}")
//    public ResponseEntity<String> cancelGuestOrder(@PathVariable String orderNumber,
//                                                   @RequestParam String phone) {
//        orderService.cancelGuestOrder(orderNumber, phone);
//
//        return ResponseEntity.ok("주문이 취소되었습니다.");
//    }

    //비회원 주문 취소 상세 페이지
//    @Override
//    @GetMapping("/getCancel/{orderNumber}")
//    public ResponseEntity<OrderCancelResponse> getGuestOrderCancel(@PathVariable String orderNumber, @RequestParam String phone){
//        OrderCancelResponse orderCancelResponse = orderService.getGuestCancelPage(orderNumber, phone);
//
//        return ResponseEntity.ok(orderCancelResponse);
//    }
}
