package com.jishop.member.service.impl;

import com.jishop.member.service.OAuthProfile;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoProfile implements OAuthProfile {

    private final String providerId;
    private final String email;
    private final String name;

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getEmail() {
        return email != null ? email : ""; // null 대신 빈 문자열 반환
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }
}
