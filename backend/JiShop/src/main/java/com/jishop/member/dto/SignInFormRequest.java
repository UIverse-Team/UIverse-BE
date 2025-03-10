package com.jishop.member.dto;


public record SignInFormRequest(
        String loginId,
        String password
) {
}
