package com.jishop.member.dto.request;

public record OAuthCallbackRequest(
        String provider,
        String code
) {
}
