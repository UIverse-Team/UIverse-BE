package com.jishop.order.controller;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.order.dto.OrderDetailResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    //주문 생성
    @Override
    @PostMapping
    public ResponseEntity<OrderResponse> create(@CurrentUser User user,
                                    @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(user, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //주문 내역 단건 조회
    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetailResponse>> getOrder(@CurrentUser User user, @PathVariable Long orderId){
        List<OrderDetailResponse> orderDetailResponse = orderService.getOrder(user, orderId);

        return ResponseEntity.ok(orderDetailResponse);
    }

    //주문 전체 조회
    @Override
    @GetMapping("/lists")
    public ResponseEntity<List<OrderResponse>> getOrderList(@CurrentUser User user,
                                          @RequestParam(value = "period", defaultValue = "all")String period){
        List<OrderResponse> responseList = orderService.getAllOrders(user, period);

        return ResponseEntity.ok(responseList);
    }

    //주문 취소
    @Override
    @PatchMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@CurrentUser User user, @PathVariable Long orderId){
        orderService.cancelOrder(user, orderId);

        return ResponseEntity.ok("주문이 취소되었습니다");
    }

}
