package com.jishop.product.dto.request;

import jakarta.validation.constraints.Pattern;

import java.util.List;

public record ProductRequest(
        String keyword,
        @Pattern(regexp = "^(wish|discount|latest|priceAsc|priceDesc)$",
                 message = "정렬 기준은 'wish', 'discount', 'latest', 'priceAsc', 'priceDesc' 중 하나여야 합니다")
        String sort,
        Long categoryId,
        // 가격 필터 (값 범위: 0~25000, 25000~50000, 50000~100000, 100000~)
        List<Integer> priceRanges,
        // 평점 필터 (값 범위: 1~5)
        List<Integer> ratings
){
        public ProductRequest {
                if (keyword == null) { keyword = ""; }
        }
}
