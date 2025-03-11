package com.jishop.order.domain;

public enum OrderStatus {

    ORDER_RECEIVED,             // 주문 접수
    ORDER_COMPLETED,            // 주문 완료
    ORDER_CANCELED,             // 주문 취소
    PURCHASED_CONFIRMED;        // 구매 확정
}
