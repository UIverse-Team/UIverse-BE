package com.jishop.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record Step1Request(
        @Email
        @NotBlank
        String email
) {
}


