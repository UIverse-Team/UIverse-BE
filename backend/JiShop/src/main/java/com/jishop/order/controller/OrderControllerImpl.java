package com.jishop.order.controller;

import com.jishop.address.domain.Address;
import com.jishop.address.dto.AddressResponse;
import com.jishop.address.repository.AddressRepository;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.order.dto.InstantOrderRequest;
import com.jishop.order.dto.OrderDetailResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // 바로 구매하기
    @Override
    @PostMapping("/instant")
    public ResponseEntity<OrderResponse> createInstantOrder(@CurrentUser User user, @RequestBody @Valid InstantOrderRequest orderRequest) {

        OrderResponse orderResponse = orderService.createInstantOrder(user, orderRequest);

        return ResponseEntity.ok(orderResponse);
    }

    // 기본 배송지 가져오기
    @GetMapping("/default-address")
    public ResponseEntity<AddressResponse> getDefaultAddress(@CurrentUser User user) {
        Optional<Address> address = addressRepository.findDefaultAddressByUser(user);
        if (address.isPresent()) {
            AddressResponse response = new AddressResponse(
                    address.get().getRecipient(),
                    address.get().getPhone(),
                    address.get().getZonecode(),
                    address.get().getAddress(),
                    address.get().getDetailAddress(),
                    address.get().isDefaultYN()
            );
            return ResponseEntity.ok(response);
        }
        throw new DomainException(ErrorType.DEFAULTADDRESS_NOT_FOUND);
    }
}
