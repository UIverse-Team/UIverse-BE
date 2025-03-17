package com.jishop.product.dto;

import jakarta.validation.constraints.Pattern;

public record ProductListRequest(
        int page,
        int size,

        @Pattern(regexp = "^(wish|discount|latest|priceAsc|priceDesc)$",
                message = "정렬 기준은 'wish', 'discount', 'latest', 'priceAsc', 'priceDesc' 중 하나여야 합니다")
        String sort
        //TODO
        // 1. filter
){
        public ProductListRequest {
                if (page < 0 || page > 100) {page = 0;}
                if (size < 1 || size > 100) {size = 12;}
                if (sort == null || sort.isEmpty()) {sort = "wish";}
        }
}
