package com.jishop.order.service;

import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderProductResponse;

import java.util.List;

public interface OrderUtilService {

    String generateOrderNumber();
    List<OrderDetail> processOrderDetails(Order order, List<OrderDetailRequest> orderDetailRequestList);
    List<OrderProductResponse> convertToOrderDetailResponses(List<OrderDetail> details, User user);
    void validateOrderCancellation(Order order);
}
