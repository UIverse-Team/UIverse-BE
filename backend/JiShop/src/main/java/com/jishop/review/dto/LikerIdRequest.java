package com.jishop.review.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LikerIdRequest(
        @NotNull(message = "likerId는 필수입니다.")
        @Positive(message = "likerId는 1이상 입니다.")
        Long likerId) {
}
