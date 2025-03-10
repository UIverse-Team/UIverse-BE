package com.jishop.order.domain;

import com.jishop.common.util.BaseEntity;
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    //대표상품명
    private String mainProductName;
    //총 주문 금액
    private int totalPrice;
    //수령인
    @Column(nullable = false)
    private String receiver;
    //수령인 연락처
    @Column(nullable = false)
    private String receiverNumber;
    //우편번호
    @Column(nullable = false)
    private String zipCode;
    //기본주소
    @Column(nullable = false)
    private String baseAddress;
    //상세주소
    @Column(nullable = false)
    private String detailAddress;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();
    @OneToOne
    private OrderNumber orderNumber;

    public void updateOrderInfo(String mainProductName, int totalPrice, List<OrderDetail> orderDetails, OrderNumber orderNumber) {
        this.mainProductName = mainProductName;
        this.totalPrice = totalPrice;
        this.orderDetails.clear();
        this.orderDetails.addAll(orderDetails);
        this.orderNumber = orderNumber;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    @Builder
    public Order(String mainProductName, int totalPrice, String receiver, String receiverNumber,
                 String zipCode, String baseAddress, String detailAddress) {
        this.status = OrderStatus.ORDER_RECEIVED;
        this.mainProductName = mainProductName;
        this.totalPrice = totalPrice;
        this.receiver = receiver;
        this.receiverNumber = receiverNumber;
        this.zipCode = zipCode;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
    }
}
