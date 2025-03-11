package com.jishop.order.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.product.domain.Product;
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
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    //수량
    private int quantity;
    //가격
    private int price;
    //할인유형
    //할인값

    @Builder
    public OrderDetail(Order order, Product product, int quantity, int price){
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
