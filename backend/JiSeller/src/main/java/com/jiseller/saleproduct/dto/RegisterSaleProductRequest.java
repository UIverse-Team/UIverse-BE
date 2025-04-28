package com.jiseller.saleproduct.dto;

import com.jiseller.product.domain.Product;
import com.jiseller.saleproduct.domain.SaleProduct;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RegisterSaleProductRequest(
        @NotBlank(message = "상품명은 필수입니다")
        String name,
        List<Long> optionIds
) {
    public SaleProduct toEntity(Product product, Option option) {
        return new SaleProduct(product.productInfo.name, option);
//                .product(product)
//                .option()
//                .build();
    }
}
