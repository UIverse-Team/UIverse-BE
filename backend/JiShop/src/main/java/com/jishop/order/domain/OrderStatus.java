package com.jishop.order.domain;

public enum OrderStatus {

//    ORDER_RECEIVED,             // 주문 접수
    PAYMENT_PENDING,            // 결제 대기
    PAYMENT_COMPLETED,          // 결제 완료
    PRODUCT_PREPARING,          // 상품 준비 중
    SHIPMENT_STARTED,           // 배송 시작
    SHIPMENT_PROCESSING,        // 배송 중
    DELIVERED,                  // 배송 완료
    ORDER_COMPLETED,            // 주문 완료
    ORDER_CANCELED,             // 주문 취소
    PURCHASED_CONFIRMED;        // 구매 확정
}
