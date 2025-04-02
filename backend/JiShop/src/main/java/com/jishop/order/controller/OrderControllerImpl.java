package com.jishop.order.controller;

import com.jishop.cart.dto.CartResponse;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import com.jishop.order.service.OrderService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    //주문 생성 - 결제 페이지와 연동
    @Override
    @PostMapping
    public void createOrder(@CurrentUser User user,
                            @RequestBody @Valid OrderRequest orderRequest,
                            HttpServletResponse response) throws IOException {
        OrderResponse orderResponse = orderService.createOrder(user, orderRequest);
        response.sendRedirect("/orders/checkout/view?orderNumber="
                + orderResponse.orderNumber()
                + "&amount=" + orderResponse.totalPrice());
    }

//    @Override
//    @PostMapping
//    public ResponseEntity<OrderResponse> createOrder(@CurrentUser User user,
//                                                     @Valid @RequestBody OrderRequest orderRequest) {
//        OrderResponse orderResponse = orderService.createOrder(user, orderRequest);
//
//        return ResponseEntity.ok(orderResponse);
//    }

    //주문 내역 단건 조회 - 결제 페이지와 연동
    @Override
    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDetailPageResponse> getOrder(@CurrentUser User user, @PathVariable String orderNumber){
        OrderDetailPageResponse orderDetailResponse = orderService.getOrder(user, orderNumber, null);

        return ResponseEntity.ok(orderDetailResponse);
    }
    
//    //주문 내역 단건 조회
//    @Override
//    @GetMapping("/{orderId}")
//    public ResponseEntity<OrderDetailPageResponse> getOrder(@CurrentUser User user, @PathVariable Long orderId){
//        OrderDetailPageResponse orderDetailResponse = orderService.getOrder(user, orderId, null, null);
//
//        return ResponseEntity.ok(orderDetailResponse);
//    }

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
//    @Override
//    @PatchMapping("/{orderId}")
//    public ResponseEntity<String> cancelOrder(@CurrentUser User user, @PathVariable Long orderId){
//        orderService.cancelOrder(user, orderId, null, null);
//
//        return ResponseEntity.ok("주문이 취소되었습니다");
//    }

    // 바로 구매하기
    @Override
    @PostMapping("/instant")
    public ResponseEntity<OrderResponse> createInstantOrder(@CurrentUser User user, @RequestBody @Valid InstantOrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createInstantOrder(user, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //회원 주문 취소 상세  페이지
//    @Override
//    @GetMapping("/getCancel/{orderId}")
//    public ResponseEntity<OrderCancelResponse> getOrderCancel(@CurrentUser User user, @PathVariable Long orderId) {
//        OrderCancelResponse orderCancelResponse = orderService.getCancelPage(user, orderId, null, null);
//
//        return ResponseEntity.ok(orderCancelResponse);
//    }

    //장바구니에서 주문서로 넘어갈 때 사용하는 API
    @PostMapping("/checkout")
    public ResponseEntity<CartResponse> getCheckout(@CurrentUser User user, @RequestBody List<OrderDetailRequest> orderDetailRequest) {
        CartResponse products = orderService.getCheckout(user, orderDetailRequest);

        return ResponseEntity.ok(products);
    }

    //바로주문하기에서 주문서로 넘어갈 때 사용하는 API
    @Override
    @GetMapping("/checkoutInstant")
    public ResponseEntity<CartResponse> getCheckoutInstant(@CurrentUser User user,
                                                           @RequestParam("saleProductId") Long saleProductId,
                                                           @RequestParam("quantity") int quantity) {
        CartResponse products = orderService.getCheckoutInstant(user, saleProductId, quantity);

        return ResponseEntity.ok(products);
    }
}