package com.jishop.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserPhoneRequest(
        @NotBlank
        String phone
) {
}
