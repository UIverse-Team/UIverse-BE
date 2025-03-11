package com.jishop.product.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "sales_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleProduct extends BaseEntity {

    String name;
    String image;
    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Product product;
    @JoinColumn(name = "option_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Option option;
    @OneToOne(mappedBy = "saleProduct")
    Stock stock;

    @Builder
    public SaleProduct(Product product, Option option, String name, String image ) {
        this.product = product;
        this.option = option;
        this.name = name;
        this.image = image;

    }
}
