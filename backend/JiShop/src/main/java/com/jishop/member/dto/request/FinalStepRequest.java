package com.jishop.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FinalStepRequest(
        @NotBlank
        String name,
        @NotBlank
        String yynumber,
        @NotBlank
        String gender,
        @NotBlank
        String phone
) {
}
