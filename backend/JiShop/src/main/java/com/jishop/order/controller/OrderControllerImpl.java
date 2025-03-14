package com.jishop.order.controller;

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
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //주문 내역 단건 조회
    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId){
        OrderResponse orderResponse = orderService.getOrder(orderId);

        return ResponseEntity.ok(orderResponse);
    }

    //주문 전체 조회
    @Override
    @GetMapping("/lists")
    public ResponseEntity<List<OrderResponse>> getOrderList(){
        List<OrderResponse> responseList = orderService.getAllOrders();

        return ResponseEntity.ok(responseList);
    }

    //주문 취소
    @Override
    @PatchMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId){
        orderService.cancelOrder(orderId);

        return ResponseEntity.ok("주문이 취소되었습니다");
    }

}
