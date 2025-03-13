package com.jishop.member.dto.request;


import jakarta.validation.constraints.NotNull;

public record SignInFormRequest(
        @NotNull
        String loginId,
        @NotNull
        String password
) {
}
