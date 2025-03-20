package com.jishop.order.dto;

import com.jishop.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailPageResponse(
        String orderNumber,
        OrderStatus orderStatus,
        int totalPrice,
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
}
