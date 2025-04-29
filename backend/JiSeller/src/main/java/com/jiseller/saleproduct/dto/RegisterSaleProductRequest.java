package com.jiseller.saleproduct.dto;

import com.jiseller.option.domain.Option;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.product.domain.Product;

public record RegisterSaleProductRequest(
        Product product
) {
    public SaleProduct toEntity(Product product, Option option) {
        return new SaleProduct(
                product.getProductInfo().getName(),
                product,
                option
        );
    }
}
