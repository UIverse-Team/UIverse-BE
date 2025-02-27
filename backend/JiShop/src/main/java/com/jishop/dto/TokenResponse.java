package com.jishop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public record TokenResponse(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_in")
        Integer expiresIn,
        @JsonProperty("refresh_token")
        String refreshToken
) {

}
