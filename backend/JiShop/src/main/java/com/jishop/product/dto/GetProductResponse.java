package com.jishop.product.dto;

import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;

public record GetProductResponse(
        String name,
        Labels labels,
        //TODO
        // 1.상품 평점
        // 2.상품 리뷰 건수
        String description,
        Integer originPrice,
        Integer discountPrice,
        Boolean isDiscount,
        String brand,
        String mainImage,
        String image1,
        String image2,
        String image3,
        String image4,
        String detailImage
) {
    public static GetProductResponse from(Product product) {
        return new GetProductResponse(
                product.getName(),
                product.getLabels(),
                product.getDescription(),
                product.getOriginPrice(),
                product.getDiscountPrice(),
                product.getIsDiscount(),
                product.getBrand(),
                product.getMainImage(),
                product.getImage1(),
                product.getImage2(),
                product.getImage3(),
                product.getImage4(),
                product.getDetailImage()
        );
    }
}
