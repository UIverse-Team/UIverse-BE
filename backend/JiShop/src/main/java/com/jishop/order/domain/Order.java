package com.jishop.order.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.member.domain.User;
import com.jishop.order.dto.OrderRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Long userId;

    //총 주문 금액
    private int totalOrderPrice;

    //총 할인 금액
    private int totalDiscountPrice;

    //총 결제 금액 (총 주문 금액 - 총 할인 금액)
    private int totalPaymentPrice;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(unique = true)
    private String orderNumber;

    public void updateStatus(OrderStatus status, LocalDateTime time) {
        this.status = status;
        this.setUpdatedAt(time);
    }

    // 주문 정보 업데이트 메서드
    public void updateOrderInfo(int totalPrice, int totalDiscountPrice, int totalPaymentPrice, List<OrderDetail> orderDetails, String orderNumber) {
        this.totalOrderPrice = totalPrice;
        this.totalDiscountPrice = totalDiscountPrice;
        this.totalPaymentPrice = totalPaymentPrice;
        if(orderDetails != null) {
            this.orderDetails = orderDetails;
        }
        this.orderNumber = orderNumber;
    }

    @Builder
    public Order(Long userId, int totalPrice, String recipient, String phone,
                 String zonecode, String address, String detailAddress, String orderNumber) {
        this.userId = userId;
        this.status = OrderStatus.ORDER_RECEIVED;
        this.totalOrderPrice = totalPrice;
        this.recipient = recipient;
        this.phone = phone;
        this.zonecode = zonecode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.orderNumber = orderNumber;
    }

    public static Order from(OrderRequest request, User user, String orderNumber) {
        return Order.builder()
                .userId(user != null ? user.getId() : null)
                .recipient(request.address().recipient())
                .phone(request.address().phone())
                .address(request.address().address())
                .detailAddress(request.address().detailAddress())
                .zonecode(request.address().zonecode())
                .orderNumber(orderNumber)
                .build();
    }


}
