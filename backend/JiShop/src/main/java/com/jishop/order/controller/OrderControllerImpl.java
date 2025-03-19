package com.jishop.order.controller;

import com.jishop.address.domain.Address;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;
    private final AddressRepository addressRepository;

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

    //주문 전체 조회 (페이징 처리)
    @Override
    @GetMapping("/lists")
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
        orderService.cancelOrder(user, orderId);

        return ResponseEntity.ok("주문이 취소되었습니다");
    }

    // 바로 구매하기
    @Override
    @PostMapping("/instant")
    public ResponseEntity<OrderResponse> createInstantOrder(@CurrentUser User user, @RequestBody @Valid InstantOrderRequest orderRequest) {

        OrderResponse orderResponse = orderService.createInstantOrder(user, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    //비회원 구매하기
    @Override
    @PostMapping("/guest")
    public ResponseEntity<OrderResponse> guestCreateOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse response = orderService.createGuestOrder(orderRequest);

        return ResponseEntity.ok(response);
    }

    //비회원 주문 조회하기
    @Override
    @GetMapping("/guest/{orderNumber}")
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetail(@PathVariable String orderNumber,
                                                                    @RequestParam String phone) {
        List<OrderDetailResponse> orderDetailList = orderService.getGuestOrder(orderNumber, phone);

        return ResponseEntity.ok(orderDetailList);
    }

    //비회원 바로 주문하기
    @Override
    @PostMapping("/guest/instant")
    public ResponseEntity<OrderResponse> guestCreateInstantOrder(@RequestBody @Valid InstantOrderRequest orderRequest) {
        OrderResponse response = orderService.createGuestInstantOrder(orderRequest);

        return ResponseEntity.ok(response);
    }

    //비회원 주문 취소하기
    @Override
    @PatchMapping("/guest/{orderNumber}")
    public ResponseEntity<String> cancelGuestOrder(@PathVariable String orderNumber,
                                 @RequestParam String phone) {
        orderService.cancelGuestOrder(orderNumber, phone);

        return ResponseEntity.ok("주문이 취소되었습니다.");
    }


    // 기본 배송지 가져오기
    @GetMapping("/default-address")
    public ResponseEntity<AddressResponse> getDefaultAddress(@CurrentUser User user) {
        return addressRepository.findDefaultAddressByUser(user)
                .map(address -> new AddressResponse(
                        address.getRecipient(),
                        address.getPhone(),
                        address.getZonecode(),
                        address.getAddress(),
                        address.getDetailAddress(),
                        address.isDefaultYN()
                ))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new DomainException(ErrorType.DEFAULTADDRESS_NOT_FOUND));
    }
}
