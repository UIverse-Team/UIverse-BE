package com.jishop.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Arrays;
import java.util.List;

public record ProductRequest(

        @NotBlank
        String keyword,

        int page,
        int size,

        @Pattern(regexp = "^(wish|discount|latest|priceAsc|priceDesc)$",
                message = "정렬 기준은 'wish', 'discount', 'latest', 'priceAsc', 'priceDesc' 중 하나여야 합니다")
        String sort,
        Long category,
        // 가격 필터 (값 범위: 0~25000, 25000~50000, 50000~100000, 100000~)
        List<Integer> priceRanges,
        // 평점 필터 (값 범위: 1~5)
        List<Integer> ratings
){
        public ProductRequest {
                if (page < 0 || page > 100) {page = 0;}
                if (size < 1 || size > 100) {size = 12;}
                if (sort == null || sort.isEmpty()) {sort = "wish";}
                if (priceRanges == null || priceRanges.isEmpty()) {
                        priceRanges = Arrays.asList(0, 25000, 50000, 100000);
                }
                if (ratings == null || ratings.isEmpty()) {ratings = Arrays.asList(1, 2, 3, 4, 5);}
        }
}
