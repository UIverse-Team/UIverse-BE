package com.jishop.order.service.impl;

import com.jishop.address.repository.AddressRepository;
import com.jishop.cart.repository.CartRepository;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderProductResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.order.repository.OrderRepository;
import com.jishop.order.service.DistributedLockService;
import com.jishop.order.service.OrderCreationService;
import com.jishop.order.service.OrderUtilService;
import com.jishop.saleproduct.repository.SaleProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderCreationServiceImpl implements OrderCreationService {

    private final OrderRepository orderRepository;
    private final SaleProductRepository saleProductRepository;
    private final AddressRepository addressRepository;
    private final OrderUtilService orderUtilService;
    private final DistributedLockService distributedLockService;
    private final CartRepository cartRepository;

    // 주문 생성 - 통합
    @Override
    @Transactional
    public OrderResponse createOrder(User user, OrderRequest orderRequest) {
        //상품 Id 목록 가져오기
        List<Long> productIds = orderRequest.orderDetailRequestList().stream()
                .map(OrderDetailRequest::saleProductId)
                .toList();

        //락 키 생성(상품 ID 목록을 기반으로)
        String lockKey = "order:creation:" + String.join("-", productIds.stream().map(String::valueOf).toList());

        //분산 락을 사용하여 주문 생성 처리
        return distributedLockService.executeWithLock(lockKey, () -> processOrderCreation(user, orderRequest));
    }

    public OrderResponse processOrderCreation(User user, OrderRequest orderRequest) {
        // 주소 저장 (회원인 경우만)
        if (user != null) {
            addressRepository.save(orderRequest.address().toEntity(user, false));
        }

        // 주문 entity 생성
        Order order = Order.from(orderRequest, user, orderUtilService.generateOrderNumber());

        // 주문 상세 처리
        List<OrderDetail> orderDetails = orderUtilService.processOrderDetails(order, orderRequest.orderDetailRequestList());
        order.getOrderDetails().addAll(orderDetails);

        // 저장하기 전에 총액 계산
        int totalOrderPrice = orderDetails.stream()
                .mapToInt(item -> item.getQuantity() * item.getOrderPrice())
                .sum();

        int totalDiscountPrice = orderDetails.stream()
                .mapToInt(item -> item.getQuantity() * item.getDiscountPrice())
                .sum();

        int totalPaymentPrice = orderDetails.stream()
                .mapToInt(item -> item.getQuantity() * item.getPaymentPrice())
                .sum();

        // 계산된 총액으로 주문 업데이트
        order.updateOrderInfo(totalOrderPrice, totalDiscountPrice, totalPaymentPrice, null, order.getOrderNumber());

        // 이제 모든 필수 필드가 설정된 상태로 주문 저장
        orderRepository.save(order);

        List<Long> cartIds = orderRequest.orderDetailRequestList().stream()
                .map(OrderDetailRequest::cartId)
                .filter(Objects::nonNull)
                .toList();

        if(!cartIds.isEmpty()) {
            cartRepository.deleteAllById(cartIds);
        }

        // 응답 생성
        List<OrderProductResponse> orderProductResponses = orderUtilService.convertToOrderDetailResponses(order.getOrderDetails(), user);
        return OrderResponse.fromOrder(order, orderProductResponses);
    }

    // 바로 주문하기 (회원/비회원 통합)
    @Override
    @Transactional
    public OrderResponse createInstantOrder(User user, OrderRequest instantOrderRequest) {
        //락키 생성 (상품 ID를 기반으로)
        String lockKey = "order:instant:" + instantOrderRequest.orderDetailRequestList().get(0).saleProductId();

        return distributedLockService.executeWithLock(lockKey, () -> processOrderCreation(user, instantOrderRequest));
    }
}
