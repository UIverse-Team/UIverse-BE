package com.jishop.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        int page,
        int size,

        @Pattern(regexp = "^(wish|discount|latest|priceAsc|priceDesc)$",
                message = "정렬 기준은 'wish', 'discount', 'latest', 'priceAsc', 'priceDesc' 중 하나여야 합니다")
        String sort,

        @NotBlank(message = "검색어를 입력해주세요")
        @Size(min = 1, max = 100, message = "검색어는 1자 이상 100자 이하여야 합니다")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s]*$", message = "검색어는 한글, 영문, 숫자, 공백만 허용됩니다")
        String keyword

        //TODO
        // 1. filter
){
        public ProductRequest {
                if (page < 0 || page > 100) {page = 0;}
                if (size < 1 || size > 100) {size = 12;}
                if (sort == null || sort.isEmpty()) {sort = "wish";}
        }
}
