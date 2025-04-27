package com.jiseller.saleproduct.dto;

import com.jiseller.product.domain.Product;
import com.jiseller.saleproduct.domain.SaleProduct;
import jakarta.validation.constraints.NotBlank;

public record SaleProductRequest(
        @NotBlank(message = "상품명은 필수입니다")
        String name,
        Long optionId
) {
    public SaleProduct toEntity(Product product) {
        return SaleProduct.builder()
                .name(name)
                .product(product)
                .option(null)
                .build();
    }
}
