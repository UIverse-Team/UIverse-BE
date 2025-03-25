package com.jishop.popular.dto;

public record PopularProduct(
        Long productId,             // 상품id
        String productImageUrl,     // 상품 이미지 url
        String store,               // 스토어 상호
        String name,                // 상품명
        int originPrice,            // 상품 판매 가격
        int discountPrice,          // 상품 할인 판매 가격
        double discountRate,        // 상품 할인율
        int salesVolume,            // 상품 판매량
        double reviewRating         // 상품 리뷰 평점
) {
}