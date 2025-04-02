package com.jishop.order.service;

import com.jishop.cart.dto.CartResponse;
import com.jishop.member.domain.User;
import com.jishop.order.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    //회원
    OrderResponse createOrder(User user, OrderRequest orderRequest);
    OrderResponse createInstantOrder(User user, InstantOrderRequest instantOrderRequest);
//    OrderDetailPageResponse getOrder(User user, Long orderId, String orderNumber, String phone);
    OrderDetailPageResponse getOrder(User user, String orderNumber, String phone);
    Page<OrderResponse> getPaginatedOrders(User user, String period, int page, int size);
//    void cancelOrder(User user, Long orderId, String orderNumber, String phone);
//    OrderCancelResponse getCancelPage(User user, Long orderId, String orderNumber, String phone);
    CartResponse getCheckout(User user, List<OrderDetailRequest> orderDetailRequest);
    CartResponse getCheckoutInstant(User user, Long saleProductId, int quantity);

    //비회원
    OrderResponse createGuestOrder(OrderRequest orderRequest);
    OrderResponse createGuestInstantOrder(InstantOrderRequest orderRequest);
    OrderDetailPageResponse getGuestOrder(String orderNumber, String phone);
//    void cancelGuestOrder(String orderNumber, String phone);
//    OrderCancelResponse getGuestCancelPage(String orderNumber, String phone);
    CartResponse getGuestCheckout(User user, List<OrderDetailRequest> orderDetailRequest);
    CartResponse getGuestCheckoutInstant(User user,  Long saleProductId, int quantity);
}
