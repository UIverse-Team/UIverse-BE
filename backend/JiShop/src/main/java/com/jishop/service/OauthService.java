package com.jishop.service;

import com.jishop.dto.SocialUserInfo;

public interface OauthService {

    SocialUserInfo authenticateUser(String code, String state);

    String generateStateAndGetAuthUrl();
}
