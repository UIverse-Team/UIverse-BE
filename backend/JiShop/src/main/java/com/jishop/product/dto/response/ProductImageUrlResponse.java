package com.jishop.product.dto.response;

import com.jishop.product.domain.embed.ImageUrl;

public record ProductImageUrlResponse(
        String mainImage,
        String image1,
        String image2,
        String image3,
        String image4,
        String detailImage
) {
    public static ProductImageUrlResponse from(ImageUrl imageUrl) {
        return new ProductImageUrlResponse(
                imageUrl.getMainImage(),
                imageUrl.getImage1(),
                imageUrl.getImage2(),
                imageUrl.getImage3(),
                imageUrl.getImage4(),
                imageUrl.getDetailImage()
        );
    }
}
