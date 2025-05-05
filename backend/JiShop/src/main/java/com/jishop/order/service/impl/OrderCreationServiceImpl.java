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

import java.util.ArrayList;
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

        //1. 락 획득 전 재고 확인
        Map<Long, Integer> productQuantityMap = orderDetails.stream()
                .collect(Collectors.toMap(
                        OrderDetailRequest::saleProductId,
                        OrderDetailRequest::quantity
                ));

        //락 없이 재고 확인 (빠른 실패)
        boolean preStockCheck = productQuantityMap.entrySet().stream()
                .allMatch(entry -> redisStockService.checkStock(entry.getKey(), entry.getValue()));

        if (!preStockCheck)
            throw new DomainException(ErrorType.INSUFFICIENT_STOCK);

        //2. 필요한 상품 Id 목록 가져오기 (재고 처리용 락키)
        List<Long> productIds = orderDetails.stream()
                .map(OrderDetailRequest::saleProductId)
                .sorted() //정렬하여 교착상태 방지
                .toList();

        //3. 주문 처리 및 재고 감소
        try {
            //상품별로 개별 락 처리하여 전체 로직 성능 개선
            OrderResponse response = processOrderCreation(user, orderRequest);

            //재고 차감 (한번에 여러 락 획득 대신 상품별 락 획득)
            List<String> failedProducts = new ArrayList<>();

            for (Long productId : productIds) {
                Integer quantity = productQuantityMap.get(productId);
                String lockKey = "order:stock:" + productId;

                try {
                    //상품별로 개별적으로 락 획득
                    boolean stockDecreased = distributedLockService.executeWithLock(lockKey, () -> {
                        //락 획득 후 다시 한번 재고 확인
                        if (!redisStockService.checkStock(productId, quantity))
                            return false;

                        // 재고 감소 처리
                        if (!redisStockService.decreaseStock(productId, quantity))
                            return false;

                        //비동기로 DB 동기화 처리
                        redisStockService.syncStockDecrease(productId, quantity);
                        return true;
                    });

                    if (!stockDecreased)
                        failedProducts.add(productId.toString());
                } catch (Exception e) {
                    log.error("상품 ID {} 재고 처리 중 오류: {} ", productId, e.getMessage());
                    failedProducts.add(productId.toString());
                }
            }

            if (!failedProducts.isEmpty()) {
                throw new DomainException(ErrorType.INSUFFICIENT_STOCK);
            }

            return response;
        } catch (Exception e) {
            log.error("주문 처리 중 오류 발생: {} ", e.getMessage());
            throw e;
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