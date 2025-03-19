package com.jishop.product.dto;

import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;
import com.jishop.productwishlist.domain.ProductWishList;

public record ProductResponse(
        String name,
        Labels labels,
        String description,
        Integer originPrice,
        Integer discountPrice,
        Boolean isDiscount,
        String brand,
        String[] images,
        String detailImage,
        Boolean isWished
) {
    public static ProductResponse from(Product product, ProductWishList productWishList) {

        boolean isWished = productWishList != null && productWishList.isProductWishStatus();

        return new ProductResponse(
                product.getName(),
                product.getLabels(),
                product.getDescription(),
                product.getOriginPrice(),
                product.getDiscountPrice(),
                product.getIsDiscount(),
                product.getBrand(),
                new String[] {
                        product.getMainImage(), product.getImage1(), product.getImage2(), product.getImage3(), product.getImage4()
                },
                product.getDetailImage(),
                isWished
        );
    }
}
