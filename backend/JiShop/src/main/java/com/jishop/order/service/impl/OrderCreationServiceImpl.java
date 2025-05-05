package com.jishop.order.service.impl;

import com.jishop.address.repository.AddressRepository;
import com.jishop.cart.repository.CartRepository;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
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
import com.jishop.stock.service.RedisStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreationServiceImpl implements OrderCreationService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderUtilService orderUtilService;
    private final DistributedLockService distributedLockService;
    private final CartRepository cartRepository;
    private final RedisStockService redisStockService;

    //비회원 주문 생성
    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        return createOrder(null, orderRequest);
    }

    // 회원 주문 생성
    @Override
    @Transactional
    public OrderResponse createOrder(User user, OrderRequest orderRequest) {

        List<OrderDetailRequest> orderDetails = orderRequest.orderDetailRequestList();
        //상품 Id 목록 가져오기 (재고 처리용 락키)
        List<Long> productIds = orderDetails.stream()
                .map(OrderDetailRequest::saleProductId)
                .sorted() //정렬하여 교착상태 방지
                .toList();

        //재고 처리를 위한 락 키 생성
        List<String> lockKeys = productIds.stream()
                .map(id -> "order:stock:" + id)
                .toList();

        //분산 락을 사용하여 주문 생성 처리
        return distributedLockService.executeWithMultipleLocks(lockKeys, () -> {
            try {
                // 1. 상품별 수량 맵 생성
                Map<Long, Integer> productQuantityMap = orderDetails.stream()
                        .collect(Collectors.toMap(
                                OrderDetailRequest::saleProductId,
                                OrderDetailRequest::quantity
                        ));

                // 2. 재고 확인 (하나라도 부족하면 예외)
                boolean hasInsufficientStock = productQuantityMap.entrySet().stream()
                        .anyMatch(entry -> !redisStockService.checkStock(entry.getKey(), entry.getValue()));

                if (hasInsufficientStock)
                    throw new DomainException(ErrorType.INSUFFICIENT_STOCK);

                // 3. 주문 생성
                OrderResponse response = processOrderCreation(user, orderRequest);

                // 4. 재고 차감 (실패 시 예외)
                for (Map.Entry<Long, Integer> entry : productQuantityMap.entrySet()) {
                    if (!redisStockService.decreaseStock(entry.getKey(), entry.getValue()))
                        throw new DomainException(ErrorType.INSUFFICIENT_STOCK);
                    redisStockService.syncStockDecrease(entry.getKey(), entry.getValue());
                }

                return response;

            } catch (Exception e) {
                log.error("주문 처리 중 오류 발생: {}", e.getMessage());
                throw e; //트랜잭션 롤백을 위해 예외 다시 던지기
            }
        });
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
}