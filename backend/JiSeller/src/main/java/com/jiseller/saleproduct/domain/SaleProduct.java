package com.jiseller.saleproduct.domain;

import com.jiseller.common.util.BaseEntity;
import com.jiseller.option.domain.Option;
import com.jiseller.product.domain.Product;
import com.jiseller.stock.domain.Stock;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleProduct extends BaseEntity {

    private String name;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name = "option_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Option option;

    @OneToOne(mappedBy = "saleProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;

    public SaleProduct(String name, Product product, Option option) {
        this.name = name;
        this.product = product;
        this.option = option;
    }
}
