package com.jishop.productwishlist.dto;

import com.jishop.product.domain.Product;

public record ProductWishProductResponse(
        Long productId,
        String productName,
        String mainImage
) {
    public ProductWishProductResponse(Product product){
        this(product.getId(), product.getProductInfo().getName(), product.getImage().getMainImage());
    }
}

