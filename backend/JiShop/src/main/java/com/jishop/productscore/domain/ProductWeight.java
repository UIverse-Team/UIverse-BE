package com.jishop.productscore.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "product_weights")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductWeight extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "1.0")
    @Column(nullable = false,
            precision = 3, scale = 2,
            columnDefinition = "decimal(3,2) default 0.00")
    private BigDecimal weightValue;

    private String description;

}
