package com.jishop.order.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.domain.OrderNumber;
import com.jishop.order.domain.OrderStatus;
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderDetailResponse;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.order.repository.OrderNumberRepository;
import com.jishop.order.repository.OrderRepository;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SaleProductRepository saleProductRepository;
    private final OrderNumberService orderNumberService;
    private final OrderNumberRepository orderNumberRepository;

    //주문 생성
    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {
        //주문 번호 생성
        String orderNumberStr = orderNumberService.generateOrderNumber();

        OrderNumber orderNumber = orderNumberRepository.findByOrderNumber(orderNumberStr)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NUMBER_NOT_FOUND));

        Order order = orderRequest.toEntity();

        List<OrderDetail> orderDetails = new ArrayList<>();
        int totalPrice = 0;
        String mainProductName = "";
        List<OrderDetailRequest> orderDetailRequests = orderRequest.orderDetailRequestList();

        for(OrderDetailRequest detailRequest : orderDetailRequests){
            Long productId = detailRequest.productId();
            int quantity = detailRequest.quantity();

            //상품 조회
            SaleProduct saleProduct = saleProductRepository.findById(productId)
                    .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

            // 주문 상세 생성
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .saleProduct(saleProduct)
                    .quantity(quantity)
                    .price(saleProduct.getProduct().getDiscountPrice())
                    .build();

            orderDetails.add(orderDetail);
            totalPrice += saleProduct.getProduct().getDiscountPrice() * quantity;
        }

        // 주문 상세가 1건일 경우
        mainProductName = orderDetails.get(0).getSaleProduct().getName();

        // 주문 상세가 2건 이상일 경우
        if(orderDetailRequests.size() != 1)
            mainProductName = mainProductName + " 외 " + (orderDetailRequests.size()-1) + "건";

        //주문 정보 업데이트
        order.updateOrderInfo(mainProductName, totalPrice, orderDetails, orderNumber);

        //주문 저장
        orderRepository.save(order);

        List<OrderDetailResponse> orderDetailResponseList = order.getOrderDetails().stream()
                .map(detail -> new OrderDetailResponse(detail.getId(), detail.getSaleProduct().getId()))
                .toList();

        return OrderResponse.fromOrder(order, orderDetailResponseList);
    }

    //주문 단건 조회
    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        List<OrderDetailResponse> orderDetailResponseList = order.getOrderDetails().stream()
                .map(detail -> new OrderDetailResponse(detail.getId(), detail.getSaleProduct().getId()))
                .toList();

        return OrderResponse.fromOrder(order, orderDetailResponseList);
    }

    // 주문 내역 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    List<OrderDetailResponse> orderDetailResponseList = order.getOrderDetails().stream()
                            .map(detail -> new OrderDetailResponse(detail.getId(), detail.getSaleProduct().getId()))
                            .toList();
                    return OrderResponse.fromOrder(order, orderDetailResponseList);
                })
                .toList();

    }

   //주문취소
    @Override
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        if(order.getStatus() == OrderStatus.ORDER_CANCELED)
            throw new DomainException(ErrorType.ORDER_ALREADY_CANCELED);

        if(order.getStatus() == OrderStatus.PURCHASED_CONFIRMED)
            throw new DomainException(ErrorType.ORDER_ALREADY_CANCELED);

        //주문 상태 변경
        order.updateStatus(OrderStatus.ORDER_CANCELED);
        order.delete();
    }

}
