package com.jishop.productwishlist.dto;

import com.jishop.member.domain.User;
import com.jishop.product.domain.Product;
import com.jishop.productwishlist.domain.ProductWishList;

public record ProductWishProductRequest(
        Long productId
) {
    public ProductWishList toEntity(User user, Product product) {
        return ProductWishList.builder()
                .user(user)
                .product(product)
                .build();
    }
}
