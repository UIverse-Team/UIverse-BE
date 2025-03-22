package com.jishop.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverUserResponse(
        String resultcode,
        String message,
        @JsonProperty("response")
        NaverResponseData response
) {
        public record NaverResponseData(
                String id,
                String name,
                String email,
                String gender,
                String mobile,
                String birthyear,
                String birthday
        ) { }
}
