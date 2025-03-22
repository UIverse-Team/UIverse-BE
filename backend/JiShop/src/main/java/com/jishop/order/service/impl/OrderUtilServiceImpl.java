package com.jishop.order.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.domain.OrderStatus;
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderProductResponse;
import com.jishop.order.repository.OrderRepository;
import com.jishop.order.service.OrderUtilService;
import com.jishop.review.repository.ReviewRepository;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import com.jishop.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderUtilServiceImpl implements OrderUtilService {

    private final OrderRepository orderRepository;
    private final SaleProductRepository saleProductRepository;
    private final StockService stockService;
    private final ReviewRepository reviewRepository;

    // 주문 조회 공통 로직
    public Order findOrder(User user, Long orderId, String orderNumber, String phone) {
        if (user != null) {
            return orderRepository.findByIdWithDetailsAndProducts(user.getId(), orderId)
                    .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));
        } else {
            return orderRepository.findByOrderNumberAndPhone(orderNumber, phone)
                    .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));
        }
    }

    // 주문 번호 생성
    public String generateOrderNumber() {
        String orderTypeCode = "O"; // Order
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String formattedDate = currentDate.format(formatter);

        String randomStr = "";
        Random random = new Random();

        final int LENGTH = 5;
        final String CHARACTERS = "01346789ABCDFGHJKMNPQRSTUVWXYZ";

        do {
            StringBuilder sb = new StringBuilder(LENGTH);
            for (int i = 0; i < LENGTH; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }
            randomStr = sb.toString();
        } while (orderRepository.existsByOrderNumber(orderTypeCode + formattedDate + randomStr));

        return orderTypeCode + formattedDate + randomStr;
    }

    // OrderDetail 처리 공통 로직
    public List<OrderDetail> processOrderDetails(Order order, List<OrderDetailRequest> orderDetailRequestList) {
        List<Long> saleProductIds = orderDetailRequestList.stream()
                .map(OrderDetailRequest::saleProductId)
                .toList();

        List<SaleProduct> saleProducts = saleProductRepository.findAllByIdsForOrder(saleProductIds);
        Map<Long, SaleProduct> saleProductMap = saleProducts.stream()
                .collect(Collectors.toMap(SaleProduct::getId, sp -> sp));

        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderDetailRequest orderDetailRequest : orderDetailRequestList) {
            SaleProduct saleProduct = Optional.ofNullable(saleProductMap.get(orderDetailRequest.saleProductId()))
                    .orElseThrow(() -> new DomainException(ErrorType.PRODUCT_NOT_FOUND));

            try {
                // 수량 줄이기
                stockService.decreaseStock(saleProduct.getStock(), orderDetailRequest.quantity());
            } catch (Exception e) {
                throw new DomainException(ErrorType.STOCK_OPERATION_FAILED);
            }
            OrderDetail orderDetail = OrderDetail.from(order, saleProduct, orderDetailRequest.quantity());
            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }

    // OrderProductResponse 변환 공통 로직
    public List<OrderProductResponse> convertToOrderDetailResponses(List<OrderDetail> details, User user) {
        // 주문 상태가 구매확정인 주문만 리뷰 작성 가능
        boolean isPurchaseConfirmed = !details.isEmpty() &&
                details.get(0).getOrder().getStatus() == OrderStatus.PURCHASED_CONFIRMED;

        List<Long> orderDetailIds = details.stream().map(OrderDetail::getId).toList();
        List<Long> reviewedOrderDetailIds = isPurchaseConfirmed ?
                reviewRepository.findOrderDetailIdsWithReviews(orderDetailIds) : Collections.emptyList();

        return details.stream()
                .map(detail -> {
                    boolean canReview = isPurchaseConfirmed && !reviewedOrderDetailIds.contains(detail.getId());
                    return OrderProductResponse.from(detail, canReview);
                })
                .toList();
    }

    // 주문 취소 가능 여부 확인
    public void validateOrderCancellation(Order order) {
        if (order.getStatus() == OrderStatus.ORDER_CANCELED) {
            throw new DomainException(ErrorType.ORDER_ALREADY_CANCELED);
        }

        if (order.getStatus() == OrderStatus.PURCHASED_CONFIRMED) {
            throw new DomainException(ErrorType.ORDER_ALREADY_CONFIRMED);
        }

        // 배송 시작 후에는 취소 불가 로직 추가 가능
        if (order.getStatus() == OrderStatus.SHIPMENT_STARTED || order.getStatus() == OrderStatus.SHIPMENT_PROCESSING)
            throw new DomainException(ErrorType.ORDER_CANNOT_CANCEL_AFTER_SHIPPING);
    }
}
