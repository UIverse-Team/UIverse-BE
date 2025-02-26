package com.jishop.service.impl;

import com.jishop.client.NaverApiClient;
import com.jishop.client.NaverResourceApiClient;
import com.jishop.domain.AuthGrantType;
import com.jishop.dto.NaverAccessResponse;
import com.jishop.dto.NaverResourceResponse;
import com.jishop.property.AuthProperty;
import com.jishop.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final AuthProperty authProperty;
    private final NaverApiClient naverApiClient;
    private final NaverResourceApiClient naverResourceApiClient;

    // 인증 코드 상태 값으로 토큰 조회로 사용자 가져오기
    @Override
    public NaverResourceResponse getTokenAndResource(String code, String state) {

        // 토큰 찾기
        NaverAccessResponse accessToken = getAccessToken(code, state);
        // 토큰으로 사용자 조회
        return getResource(accessToken.getAccessToken(), accessToken.getTokenType());
    }

    //토큰 조회하기
    @Override
    public NaverAccessResponse getAccessToken(String code, String state) {

        return naverApiClient.getAccessToken(
                AuthGrantType.AUTHORIZATION_CODE.getValue(),
                authProperty.getClientId(),
                authProperty.getClientSecret(),
                code,
                state
        );
    }

    // 토큰으로 사용자 조회
    @Override
    public NaverResourceResponse getResource(String accessToken, String tokenType) {

        return naverResourceApiClient.getResource(String.format("%s %s", tokenType, accessToken));
    }
}
