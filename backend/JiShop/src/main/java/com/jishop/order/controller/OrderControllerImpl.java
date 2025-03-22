package com.jishop.order.controller;

import com.jishop.address.dto.AddressResponse;
import com.jishop.address.repository.AddressRepository;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import com.jishop.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    //주문 생성
    @Override
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@CurrentUser User user,
                                                     @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(user, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //주문 내역 단건 조회
    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailPageResponse> getOrder(@CurrentUser User user, @PathVariable Long orderId){
        OrderDetailPageResponse orderDetailResponse = orderService.getOrder(user, orderId, null, null);

        return ResponseEntity.ok(orderDetailResponse);
    }

    //주문 전체 조회 (페이징 처리)
    @Override
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrderList(
            @CurrentUser User user,
            @RequestParam(value = "period", defaultValue = "all") String period,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<OrderResponse> responseList = orderService.getPaginatedOrders(user, period, page, size);

        return ResponseEntity.ok(responseList);
    }

    //주문 취소
    @Override
    @PatchMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@CurrentUser User user, @PathVariable Long orderId){
        orderService.cancelOrder(user, orderId, null, null);

        return ResponseEntity.ok("주문이 취소되었습니다");
    }

    // 바로 구매하기
    @Override
    @PostMapping("/instant")
    public ResponseEntity<OrderResponse> createInstantOrder(@CurrentUser User user, @RequestBody @Valid InstantOrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createInstantOrder(user, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //비회원 주문 조회하기
    @Override
    @GetMapping("/guest/{orderNumber}")
    public ResponseEntity<OrderDetailPageResponse> getGuestOrderDetail(@PathVariable String orderNumber,
                                                                       @RequestParam String phone) {
        OrderDetailPageResponse orderDetailList = orderService.getOrder(null, null, orderNumber, phone);

        return ResponseEntity.ok(orderDetailList);
    }

    //비회원 주문 취소하기
    @Override
    @PatchMapping("/guest/{orderNumber}")
    public ResponseEntity<String> cancelGuestOrder(@PathVariable String orderNumber,
                                                   @RequestParam String phone) {
        orderService.cancelOrder(null, null, orderNumber, phone);

        return ResponseEntity.ok("주문이 취소되었습니다.");
    }

    //회원 주문 취소 상세  페이지
    @Override
    @GetMapping("/getCancel/{orderId}")
    public ResponseEntity<OrderCancelResponse> getOrderCancel(@CurrentUser User user, @PathVariable Long orderId) {
        OrderCancelResponse orderCancelResponse = orderService.getCancelPage(user, orderId, null, null);

        return ResponseEntity.ok(orderCancelResponse);
    }

    //비회원 주문 취소 상세 페이지
    @Override
    @GetMapping("/getGuestCancel/{orderNumber}")
    public ResponseEntity<OrderCancelResponse> getGuestOrderCancel(@PathVariable String orderNumber, @RequestParam String phone){
        OrderCancelResponse orderCancelResponse = orderService.getCancelPage(null, null, orderNumber, phone);

        return ResponseEntity.ok(orderCancelResponse);
    }
}