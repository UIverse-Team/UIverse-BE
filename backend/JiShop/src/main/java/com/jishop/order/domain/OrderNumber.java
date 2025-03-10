package com.jishop.order.domain;

import jakarta.annotation.security.DenyAll;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DenyAll
public class OrderNumber {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String orderNumber;

    @Builder
    public OrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
