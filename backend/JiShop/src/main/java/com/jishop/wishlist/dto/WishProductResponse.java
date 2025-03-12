package com.jishop.wishlist.dto;

import com.jishop.product.domain.Product;

public record WishProductResponse(
        Long productid,
        String productName,
        String mainImage
) {
    public WishProductResponse(Long productid, String productName, String mainImage){
        this.productid = productid;
        this.productName = productName;
        this.mainImage = mainImage;
    }

    public WishProductResponse(Product product){
        this(product.getId(), product.getName(), product.getMainImage());
    }
}

