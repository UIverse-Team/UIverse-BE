package com.jishop.order.controller;

import com.jishop.cart.dto.CartResponse;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import com.jishop.order.service.OrderCancelService;
import com.jishop.order.service.OrderCreationService;
import com.jishop.order.service.OrderGetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderCreationService orderCreationService;
    private final OrderGetService orderGetService;
    private final OrderCancelService orderCancelService;

    // 회원 주문 생성
    @Override
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@CurrentUser User user,
                                                     @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderCreationService.createOrder(user, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }
    
    //회원 주문 내역 단건 조회
    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailPageResponse> getOrder(@CurrentUser User user, @PathVariable Long orderId){
        OrderDetailPageResponse orderDetailResponse = orderGetService.getOrder(user, orderId);

        return ResponseEntity.ok(orderDetailResponse);
    }

    //회원 주문 전체 조회 (페이징 처리)
    @Override
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrderList(
            @CurrentUser User user,
            @RequestParam(value = "period", defaultValue = "all") String period,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<OrderResponse> responseList = orderGetService.getPaginatedOrders(user, period, page, size);

        return ResponseEntity.ok(responseList);
    }

    //회원 주문 취소
    @Override
    @PatchMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@CurrentUser User user, @PathVariable Long orderId){
        orderCancelService.cancelOrder(user, orderId);

        return ResponseEntity.ok("주문이 취소되었습니다");
    }

    // 회원 바로 구매하기
    @Override
    @PostMapping("/instant")
    public ResponseEntity<OrderResponse> createInstantOrder(@CurrentUser User user, @RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse orderResponse = orderCreationService.createInstantOrder(user, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //회원 주문 취소 상세 페이지
    @Override
    @GetMapping("/getCancel/{orderId}")
    public ResponseEntity<OrderCancelResponse> getOrderCancel(@CurrentUser User user, @PathVariable Long orderId) {
        OrderCancelResponse orderCancelResponse = orderGetService.getCancelPage(user, orderId);

        return ResponseEntity.ok(orderCancelResponse);
    }

    //장바구니에서 주문서로 넘어갈 때 사용하는 API
    @PostMapping("/checkout")
    public ResponseEntity<CartResponse> getCheckout(@CurrentUser User user, @RequestBody List<OrderDetailRequest> orderDetailRequest) {
        CartResponse products = orderGetService.getCheckout(user, orderDetailRequest);

        return ResponseEntity.ok(products);
    }

    //리뷰 작성 시 필요한 상품 정보 내려주기
    @GetMapping("/{orderDetailId}/item")
    public ResponseEntity<OrderProductResponse> getItem(@PathVariable Long orderDetailId) {
        OrderProductResponse response = orderGetService.getItem(orderDetailId);

        return ResponseEntity.ok(response);
    }


    //주문 생성 - 결제 페이지와 연동
//    @Override
//    @PostMapping
//    public void createOrder(@CurrentUser User user,
//                            @RequestBody @Valid OrderRequest orderRequest,
//                            HttpServletResponse response) throws IOException {
//        OrderResponse orderResponse = orderService.createOrder(user, orderRequest);
//        response.sendRedirect("/orders/checkout/view?orderNumber="
//                + orderResponse.orderNumber()
//                + "&amount=" + orderResponse.totalPrice());
//    }

    //주문 내역 단건 조회 - 결제 페이지와 연동
//    @Override
//    @GetMapping("/{orderNumber}")
//    public ResponseEntity<OrderDetailPageResponse> getOrder(@CurrentUser User user, @PathVariable String orderNumber){
//        OrderDetailPageResponse orderDetailResponse = orderService.getOrder(user, orderNumber, null);
//
//        return ResponseEntity.ok(orderDetailResponse);
//    }
}