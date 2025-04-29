package com.jiseller.saleproduct.dto;

import com.jiseller.option.domain.Option;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.product.domain.Product;

public record RegisterSaleProductRequest(
        Product product,
        Option option
) {
    public SaleProduct toEntity() {
        return new SaleProduct(
                product.getProductInfo().getName(),
                product,
                option
        );
    }
}
