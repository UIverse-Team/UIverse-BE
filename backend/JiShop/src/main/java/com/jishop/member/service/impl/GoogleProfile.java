package com.jishop.member.service.impl;

import com.jishop.member.service.OAuthProfile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleProfile implements OAuthProfile {

    private final String providerId;
    private final String email;
    private final String name;

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
        return "google";
    }
}
