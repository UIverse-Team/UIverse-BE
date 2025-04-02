package com.jishop.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserResponse(
        String id,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public record KakaoAccount(
            Profile profile,
            @JsonProperty(required = false)
            String email
    ) {}

    public record Profile(
            String nickname
    ) {}
}