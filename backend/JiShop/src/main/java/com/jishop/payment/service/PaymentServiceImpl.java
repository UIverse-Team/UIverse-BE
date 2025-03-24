package com.jishop.payment.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderStatus;
import com.jishop.order.repository.OrderRepository;
import com.jishop.payment.domain.Payment;
import com.jishop.payment.dto.PaymentConfirmRequest;
import com.jishop.payment.dto.PaymentConfirmResponse;
import com.jishop.payment.dto.TossConfirmRequest;
import com.jishop.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TossPaymentService tossPaymentService;

    /**
     * 클라이언트에게 전달받은 결제정보를 검증하고, 토스 결제 승인 API를 호출하는 클래스
     *
     * @param request   결제정보(paymentKey), 주문번호(orderId), 결제금액(amount)
     * @return
     */
    @Override
    public PaymentConfirmResponse confirmPayment(PaymentConfirmRequest request) {

        // 주문 정보 검증(주문번호, 결제금액)
        Order order = orderRepository.findByOrderNumber(request.orderId())
                .orElseThrow(() -> new DomainException(ErrorType.ORDER_NOT_FOUND));

        validateOrderInfo(order, request);
        
        // 토스 결제 승인 요청
        TossConfirmRequest tossConfirmRequest = TossConfirmRequest.fromPaymentConfirmRequest(request);
        Payment payment = tossPaymentService.confirmPayment(tossConfirmRequest);

        // 결제 정보 저장 및 주문 정보 업데이트
        paymentRepository.save(payment);
        order.setPayment(payment);
        order.updateStatus(OrderStatus.PAYMENT_COMPLETED, LocalDateTime.now());

        return PaymentConfirmResponse.fromPayment(payment, "결제가 승인되었습니다.");
    }

    /** 
     *  주문 정보 검증 메서드
     * 
     * @param order     주문번호로 조회한 Order 엔티티
     * @param request   결제 승인 요청 정보
     * @return
     */
    private void validateOrderInfo(Order order, PaymentConfirmRequest request) {
        // 주문번호 검증
        if(!order.getOrderNumber().equals(request.orderId())){
            throw new DomainException(ErrorType.ORDER_NUMBER_MISMATCH);
        }

        // 결제 금액 검증
        if(order.getTotalOrderPrice() != request.amount()){
            throw new DomainException(ErrorType.ORDER_AMOUNT_MISMATCH);
        }

        // 주문 상태 검증
        if(order.getStatus() != OrderStatus.PAYMENT_PENDING){
            throw new DomainException(ErrorType.ORDER_STATUS_INVAILD);
        }
    }
}
