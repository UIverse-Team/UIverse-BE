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

    // Order와 연관관계 추가
    @JoinColumn(name = "order_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    // Order엔티티의 OrderNumber(주문번호)
    @Column(nullable = false, unique = true)
    private String orderNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // 결제 요청 시간
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    // 결제 승인 시간
    @Column(nullable = false)
    private LocalDateTime approvedAt;

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

    @Builder
    public Payment(EasyPayInfo easyPay, CardInfo card,
                   int taxFreeAmount, int vat, int suppliedAmount, int totalAmount,
                   String currency, PaymentMethod method, LocalDateTime approvedAt,
                   LocalDateTime requestedAt, PaymentStatus status, String orderNumber,
                   Order order, String paymentKey) {
        this.easyPay = easyPay;
        this.card = card;
        this.taxFreeAmount = taxFreeAmount;
        this.vat = vat;
        this.suppliedAmount = suppliedAmount;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.method = method;
        this.approvedAt = approvedAt;
        this.requestedAt = requestedAt;
        this.status = status;
        this.orderNumber = orderNumber;
        this.order = order;
        this.paymentKey = paymentKey;
    }
}
