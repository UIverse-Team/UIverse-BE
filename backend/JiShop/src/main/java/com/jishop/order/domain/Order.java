package com.jishop.order.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    //주문상태
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // todo: 결제수단 매핑
    private Long paymentId;

    // todo: user 세션 가져오기
    private Long userId;

    //대표상품명
    private String mainProductName;

    //총 주문 금액
    private int totalPrice;

    //수령인
    @Column(nullable = false)
    private String recipient;

    //수령인 연락처
    @Column(nullable = false)
    private String phone;

    //우편번호
    @Column(nullable = false)
    private String zonecode;

    //기본주소
    @Column(nullable = false)
    private String address;

    //상세주소
    @Column(nullable = false)
    private String detailAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(unique = true)
    private String orderNumber;

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    // 주문 정보 업데이트 메서드
    public void updateOrderInfo(String mainProductName, int totalPrice, List<OrderDetail> orderDetails, String orderNumber) {
        this.mainProductName = mainProductName;
        this.totalPrice = totalPrice;
        this.orderDetails = orderDetails;
        this.orderNumber = orderNumber;
    }

    @Builder
    public Order(Long userId, String mainProductName, int totalPrice, String recipient, String phone,
                 String zonecode, String address, String detailAddress, String orderNumber) {
        this.userId = userId;
        this.status = OrderStatus.ORDER_RECEIVED;
        this.mainProductName = mainProductName;
        this.totalPrice = totalPrice;
        this.recipient = recipient;
        this.phone = phone;
        this.zonecode = zonecode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.orderNumber = orderNumber;
    }

    public Order withOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }
}
