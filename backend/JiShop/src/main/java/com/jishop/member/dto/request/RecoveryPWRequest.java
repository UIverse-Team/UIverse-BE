package com.jishop.member.dto.request;

import com.jishop.member.annotation.Password;
import jakarta.validation.constraints.NotBlank;

public record RecoveryPWRequest(
        @NotBlank
        @Password
        String password
) {
}
