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

import java.util.*;
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

        //1. 주문 상품 ID와 수량 매핑
        Map<Long, Integer> productQuantityMap = orderDetails.stream()
                .collect(Collectors.toMap(
                        OrderDetailRequest::saleProductId,
                        OrderDetailRequest::quantity
                ));

        //2. 락 없이 재고 확인 (빠른 실패)
        if (!redisStockService.checkMultipleStocks(productQuantityMap))
            throw new DomainException(ErrorType.INSUFFICIENT_STOCK);

        //3. 필요한 상품 Id 목록 불러오기 (락을 위해 정렬)
        List<Long> productIds = new ArrayList<>(productQuantityMap.keySet());
        Collections.sort(productIds);

        //4. 주문 생성에 필요한 준비 작업
        String lockKey = "order:stock:" + String.join(":", productIds.stream()
                .map(String::valueOf)
                .toList());

        try {
            //5. 복합 락을 사용하여 원자적 주문 처리
            return distributedLockService.executeWithLock(lockKey, () -> {
                //6. 락 획득 후 다시 재고 확인
                if (!redisStockService.checkMultipleStocks(productQuantityMap))
                    throw new DomainException(ErrorType.INSUFFICIENT_STOCK);

                //7. 주문 처리 및 DB 저장
                OrderResponse response = processOrderCreation(user, orderRequest);

                //8. 재고 차감 - 일괄 처리
                try {
                    boolean stockDecreased = redisStockService.decreaseMultipleStocks(productQuantityMap);

                    if (!stockDecreased)
                        throw new DomainException(ErrorType.INSUFFICIENT_STOCK);

                    //9. 비동기로 DB 동기화 처리
                    productQuantityMap.forEach(redisStockService::syncStockDecrease);

                    return response;
                } catch (Exception e) {
                    log.error("주문 재고 처리 중 오류 발생: {}", e.getMessage(), e);
                    throw new DomainException(ErrorType.ORDER_CREATION_FAILED);
                }
            });
        } catch (Exception e) {
            if (e instanceof DomainException)
                throw (DomainException) e;
            log.error("주문 재고 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new DomainException(ErrorType.ORDER_CREATION_FAILED);
        }
    }

    @Transactional
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