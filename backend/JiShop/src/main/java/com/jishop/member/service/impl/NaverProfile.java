package com.jishop.member.service.impl;

import com.jishop.member.service.OAuthProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NaverProfile implements OAuthProfile {

    private final String providerId;
    private final String email;
    private final String name;
    private final String gender;
    private final String mobile;
    private final String birthyear;
    private final String birthday;


    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

}
