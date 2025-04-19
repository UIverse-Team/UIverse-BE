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
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Table(name = "sales_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleProduct extends BaseEntity {

    private String name;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name = "option_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Option option;

    @OneToOne(mappedBy = "saleProduct")
    private Stock stock;

    @Builder
    public SaleProduct(Product product, Option option, String name) {
        this.product = product;
        this.option = option;
        this.name = name;
    }

    public String getProductSummary(int quantity) {
        if (option == null) {
            return String.format("%s;%s",
                    name,
                    quantity);
        }

        return String.format("%s;%s;%s",
                name,
                quantity,
                option.getOptionValue());
    }
}