package com.jishop.payment.domain;

import com.jishop.order.domain.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 토스에서 주는 응답을 기반으로 Payment 엔티티 생성
 * BaseEntity를 상속받지 않음
 */
@Entity
@Getter
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 토스에서 관리하는 고유 결제키
    @Column(nullable = false, unique = true)
    private String paymentKey;

    @Column(nullable = false)
    private String orderId;

//    @Column(nullable = false)
//    private String orderName;

    // TODO : 토스테서 전달해주는 status 응답값 확인
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // 결제 요청 시간
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    // 결제 승인 시간
    @Column(nullable = false)
    private LocalDateTime approvedAt;

    // TODO : 토스에서 전달해주는 status 응답값 확인
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private int totalAmount;

    // 과세 대상 금액
    private int suppliedAmount;

    // 부가세
    private int vat;

    // 면세 금액
    private int taxFreeAmount;

    // 카드 결제 시 추가 정보
    @Embedded
    private CardInfo card;

    // 간편 결제 시 추가 정보
    @Embedded
    private EasyPayInfo easyPay;

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private Order order;

    @Builder
    private Payment(String paymentKey, String orderId, String orderName, PaymentStatus status,
                   LocalDateTime requestedAt, LocalDateTime approvedAt,
                   PaymentMethod method, String currency, int totalAmount, int suppliedAmount, int vat, int taxFreeAmount,
                   CardInfo card, Order order, EasyPayInfo easyPay) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.method = method;
        this.currency = currency;
        this.totalAmount = totalAmount;
        this.suppliedAmount = suppliedAmount;
        this.vat = vat;
        this.taxFreeAmount = taxFreeAmount;
        this.card = card;
        this.order = order;
        this.easyPay = easyPay;
    }

    public void updateStatus(PaymentStatus status){
        this.status = status;
    }
}
