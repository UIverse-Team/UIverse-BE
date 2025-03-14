package com.jishop.wishlist.dto;

import com.jishop.product.domain.Product;

public record WishProductResponse(
        Long productId,
        String productName,
        String mainImage
) {
    public WishProductResponse(Product product){
        this(product.getId(), product.getName(), product.getMainImage());
    }
}

