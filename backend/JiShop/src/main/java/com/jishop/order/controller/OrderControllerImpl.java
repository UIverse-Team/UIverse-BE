package com.jishop.order.controller;

import com.jishop.common.exception.ErrorType;
import com.jishop.order.domain.Order;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.order.service.OrderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> create(@Valid @RequestBody OrderRequest orderRequest,
                                                HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다");
        }

        // session 값이 Integer로 저장되어있어 Long으로 변환
        Number userIdNumber = (Number) session.getAttribute("userId");
        Long userId = userIdNumber.longValue();

        OrderResponse orderResponse = orderService.createOrder(userId, orderRequest);

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
