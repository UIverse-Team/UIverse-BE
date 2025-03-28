package com.jishop.order.service;

import com.jishop.order.domain.OrderStatus;
import com.jishop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;

    //매일 특정 시간
    //@Scheduled(cron = "0 0 0 * * ?")

    @Transactional
    @Scheduled(fixedRate = 3600000) //1시간마다 실행
    public void updateOrderStatus(){
        LocalDateTime now = LocalDateTime.now();

        // PAYMENT_COMPLETED 상태에서 1일 지난 주문들 -> PRODUCT_PREPARING
        updateStatusForOrders(OrderStatus.PAYMENT_COMPLETED, OrderStatus.PRODUCT_PREPARING, now.minusDays(1));

        //PRODUCT_PREPARING 상태에서 1일 지난 주문들 -> SHIPMENT_START
        updateStatusForOrders(OrderStatus.PRODUCT_PREPARING, OrderStatus.SHIPMENT_STARTED, now.minusDays(1));

        //SHIPMENT_STARTED 상태에서 1일 지난 주문들 -> SHIPMENT_PROCESSING
        updateStatusForOrders(OrderStatus.SHIPMENT_STARTED, OrderStatus.SHIPMENT_PROCESSING, now.minusDays(1));

        //SHIPMENT_PROCESSING 상태에서 1일 지난 주문들 -> DELIVERED
        updateStatusForOrders(OrderStatus.SHIPMENT_PROCESSING, OrderStatus.DELIVERED, now.minusDays(1));
    }

    private void updateStatusForOrders(OrderStatus currentStatus,
                                       OrderStatus newStatus,
                                       LocalDateTime cutoff) {
        orderRepository.bulkUpdateStatus(
                currentStatus,
                newStatus,
                cutoff
        );
    }
}
