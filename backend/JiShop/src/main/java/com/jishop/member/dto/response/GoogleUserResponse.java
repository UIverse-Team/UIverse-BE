package com.jishop.member.dto.response;

public record GoogleUserResponse(
        String sub,
        String email,
        String name
) {}
