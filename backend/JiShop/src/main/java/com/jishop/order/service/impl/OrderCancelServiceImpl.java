package com.jishop.order.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.domain.OrderStatus;
import com.jishop.order.repository.OrderRepository;
import com.jishop.order.service.OrderCancelService;
import com.jishop.order.service.OrderUtilService;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderCancelServiceImpl implements OrderCancelService {

    private final StockService stockService;
    private final OrderUtilService orderUtilService;
    private final OrderRepository orderRepository;

    //비회원 주문 취소
    @Override
    public void cancelOrder(String orderNumber, String phone) {
        Order order = orderRepository.findByOrderNumberAndPhone(orderNumber, phone)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        processCancellation(order);
    }

    // 회원 주문 취소
    @Override
    public void cancelOrder(User user, Long orderId) {
        Order order = orderRepository.findByIdWithDetailsAndProducts(user.getId(), orderId)
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        processCancellation(order);
    }

    private void processCancellation(Order order) {
        orderUtilService.validateOrderCancellation(order);

        try {
            // 재고 되돌리기
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                SaleProduct saleProduct = orderDetail.getSaleProduct();
                int quantity = orderDetail.getQuantity();
                stockService.increaseStock(saleProduct.getStock(), quantity);
            }

            // 주문 상태 변경
            order.updateStatus(OrderStatus.ORDER_CANCELED, LocalDateTime.now());
            order.delete();
        } catch (DomainException e) {
            throw new DomainException(ErrorType.ORDER_CANCEL_FAILED);
        }
    }
}
