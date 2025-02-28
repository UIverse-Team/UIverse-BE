package com.jishop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public record NaverUserResponse(
        String resultcode,
        String message,
        // response라는 JSON 키에 실제 사용자 정보가 들어있음
        @JsonProperty("response")
        SocialUserInfo response
) {
}