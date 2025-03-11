package com.jishop.member.service;

import com.jishop.member.dto.SocialUserInfo;

public interface OauthService {

    SocialUserInfo authenticateUser(String code, String state);

    String generateStateAndGetAuthUrl();
}
