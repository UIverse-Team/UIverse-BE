package com.jishop.product.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ProductRequest(

        @Min(value = 0, message = "페이지는 0 이상이어야 합니다")
        @Max(value = 999, message = "페이지는 999 이하여야 합니다")
        int page,

        @Min(value = 1, message = "사이즈는 1 이상이어야 합니다")
        int size,

        String sort
        //TODO
        // 1. filter
){
}
