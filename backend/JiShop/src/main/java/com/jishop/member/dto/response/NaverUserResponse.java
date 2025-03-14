package com.jishop.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverUserResponse(
        String resultcode,
        String message,
        // response라는 JSON 키에 실제 사용자 정보가 들어있음
        @JsonProperty("response")
        SocialUserInfo response
) {
}