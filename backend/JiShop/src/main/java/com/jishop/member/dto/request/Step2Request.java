package com.jishop.member.dto.request;

import com.jishop.member.annotation.Password;
import jakarta.validation.constraints.NotBlank;

public record Step2Request(
        @NotBlank
        @Password
        String password
) {
}
