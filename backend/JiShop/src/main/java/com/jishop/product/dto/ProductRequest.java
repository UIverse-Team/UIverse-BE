package com.jishop.product.dto;

import jakarta.validation.constraints.NotBlank;

public record ProductRequest(
        int page,
        int size,
        String sort,
        @NotBlank(message = "검색어를 입력해주세요")
        String keyword
        //TODO
        // 1. filter
){
        public ProductRequest {
                if (page < 0) {page = 0;}
                if (page > 100) {page = 0;}
                if (size < 0) {size = 10;}
                if (sort == null || sort.isEmpty()) {sort = "wish";}
        }
}
