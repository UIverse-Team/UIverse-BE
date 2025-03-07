package com.jishop.dto;

public record SignInFormRequest(
        String loginId,
        String password
) {
}
