package com.jishop.order.service.impl;

import com.jishop.address.repository.AddressRepository;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.dto.*;
import com.jishop.order.repository.OrderRepository;
import com.jishop.order.service.OrderCreationService;
import com.jishop.order.service.OrderUtilService;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCreationServiceImpl implements OrderCreationService {

    private final OrderRepository orderRepository;
    private final SaleProductRepository saleProductRepository;
    private final AddressRepository addressRepository;
    private final OrderUtilService orderUtilService;

    // 주문 생성 - 통합
    @Override
    @Transactional
    public OrderResponse createOrder(User user, OrderRequest orderRequest) {
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

        // 응답 생성
        List<OrderProductResponse> orderProductResponses = orderUtilService.convertToOrderDetailResponses(order.getOrderDetails(), user);
        return OrderResponse.fromOrder(order, orderProductResponses);
    }

    // 바로 주문하기 (회원/비회원 통합)
    @Override
    @Transactional
    public OrderResponse createInstantOrder(User user, InstantOrderRequest instantOrderRequest) {
        // InstantOrderRequest => OrderRequest
        OrderRequest orderRequest = convertInstantToOrderRequest(instantOrderRequest);

        return createOrder(user, orderRequest);
    }

    private OrderRequest convertInstantToOrderRequest(InstantOrderRequest instantOrderRequest) {
        SaleProduct saleProduct = saleProductRepository.findById(instantOrderRequest.saleProductId())
                .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        OrderDetailRequest detailRequest = new OrderDetailRequest(
                saleProduct.getId(), instantOrderRequest.quantity()
        );

        return new OrderRequest(
                instantOrderRequest.address(),
                List.of(detailRequest)
        );
    }
}
