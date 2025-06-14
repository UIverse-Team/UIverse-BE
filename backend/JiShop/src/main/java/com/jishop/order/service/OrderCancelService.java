package com.jishop.order.service;

import com.jishop.member.domain.User;

public interface OrderCancelService {
    //회원 주문 취소
    void cancelOrder(User user, Long orderId);

    //비회원 주문 취소
    void cancelOrder(String orderNumber, String phone);
}
