package com.jishop.order.dto;

import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailPageResponse(
        Long orderId,
        String orderNumber,
        OrderStatus orderStatus,
        int totalPrice,
        int totalDiscount,
        int totalPayment,
        LocalDateTime createdAt,
        //수신자 정보 (한 번만 표시)
        String recipient,
        String phone,
        String zoneCode,
        String address,
        String detailAddress,
        String email,
        //상품목록
        List<OrderProductResponse> products
) {
    public static OrderDetailPageResponse from(Order order, User user, List<OrderProductResponse> products) {
        return new OrderDetailPageResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalOrderPrice(),
                order.getTotalDiscountPrice(),
                order.getTotalPaymentPrice(),
                order.getCreatedAt(),
                order.getRecipient(),
                order.getPhone(),
                order.getZonecode(),
                order.getAddress(),
                order.getDetailAddress(),
                user != null ? user.getLoginId() : null,
                products
        );
    }
}
