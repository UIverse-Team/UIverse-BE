package com.jishop.order.controller;

import com.jishop.order.dto.OrderCancelResponse;
import com.jishop.order.dto.OrderDetailPageResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.order.service.OrderCancelService;
import com.jishop.order.service.OrderCreationService;
import com.jishop.order.service.OrderGetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        OrderResponse orderResponse = orderCreationService.createOrder(null, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //비회원 바로주문 생성
    @Override
    @PostMapping("/instant")
    public ResponseEntity<OrderResponse> createGuestInstantOrder(@RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse orderResponse = orderCreationService.createInstantOrder(null, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //비회원 주문 조회하기
    @Override
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDetailPageResponse> getGuestOrderDetail(@PathVariable String orderNumber,
                                                                       @RequestParam String phone) {
        OrderDetailPageResponse orderDetailList = orderGetService.getOrder(null, null, orderNumber, phone);

        return ResponseEntity.ok(orderDetailList);
    }

    //비회원 주문 취소하기
    @Override
    @PatchMapping("/{orderNumber}")
    public ResponseEntity<String> cancelGuestOrder(@PathVariable String orderNumber,
                                                   @RequestParam String phone) {
        orderCancelService.cancelOrder(null, null, orderNumber, phone);

        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    //비회원 주문 취소 상세 페이지
    @Override
    @GetMapping("/getCancel/{orderNumber}")
    public ResponseEntity<OrderCancelResponse> getGuestOrderCancel(@PathVariable String orderNumber, @RequestParam String phone){
        OrderCancelResponse orderCancelResponse = orderGetService.getCancelPage(null,null, orderNumber, phone);

        return ResponseEntity.ok(orderCancelResponse);
    }
}
