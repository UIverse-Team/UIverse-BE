package com.jishop.popular.dto;

import jakarta.validation.constraints.NotBlank;

public record SearchRequest(
        @NotBlank(message = "검색어 입력은 필수입니다.")
        String keyword
) {
}