package com.jishop.member.service;

import com.jishop.member.dto.response.SocialUserInfo;

public interface OauthService {

    SocialUserInfo authenticateUser(String code, String state);

    String generateStateAndGetAuthUrl();
}
