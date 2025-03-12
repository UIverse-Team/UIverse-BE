package com.jishop.order.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderNumber extends BaseEntity {

    @Column(unique = true)
    private String orderNumber;

    // OrderNumber 엔티티에 추가
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Builder
    public OrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
