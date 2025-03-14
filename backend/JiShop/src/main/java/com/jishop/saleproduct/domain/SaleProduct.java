package com.jishop.saleproduct.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.option.domain.Option;
import com.jishop.product.domain.Product;
import com.jishop.stock.domain.Stock;
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

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Product product;

    @JoinColumn(name = "option_id")
    @OneToOne(fetch = FetchType.LAZY)
    Option option;

    @OneToOne(mappedBy = "saleProduct")
    Stock stock;

    @Builder
    public SaleProduct(Product product, Option option, String name) {
        this.product = product;
        this.option = option;
        this.name = name;
    }
}