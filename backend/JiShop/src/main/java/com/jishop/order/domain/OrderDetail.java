package com.jishop.order.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.saleproduct.domain.SaleProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDetail extends BaseEntity {

    //주문id
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    //상품id
    @JoinColumn(name = "sale_product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SaleProduct saleProduct;

    //수량
    private int quantity;
    //결제가격
    private int paymentPrice;
    //주문가격
    private int orderPrice;
    //할인금액
    private int discountPrice;
    //할인유형
    private String discountType;
    //할인값
    private double discountValue;

    @Builder
    public OrderDetail(Order order, SaleProduct saleProduct, int quantity, int paymentPrice, int orderPrice, int discountPrice) {
        this.order = order;
        this.saleProduct = saleProduct;
        this.quantity = quantity;
        this.paymentPrice = paymentPrice;
        this.orderPrice = orderPrice;
        this.discountPrice = discountPrice;
    }


}
