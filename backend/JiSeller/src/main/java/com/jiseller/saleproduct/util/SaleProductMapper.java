package com.jiseller.saleproduct.util;

import com.jiseller.option.domain.Option;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.product.domain.Product;

public class SaleProductMapper {
    public static SaleProduct toEntity(Product product, Option option) {
        return new SaleProduct(
                product.getProductInfo().getName(),
                product,
                option
        );
    }
}
