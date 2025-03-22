package com.jishop.member.service;

import com.jishop.member.dto.response.TokenResponse;

public interface OAuthClient {

    String getAuthorizationUrl(String state); // state 값을 받도록 수정
    TokenResponse getAccessToken(String code);
    OAuthProfile getProfile(String accessToken);
}
