package com.jishop.service;

import com.jishop.dto.NaverAccessResponse;
import com.jishop.dto.NaverResourceResponse;

public interface SocialService {

    NaverResourceResponse getTokenAndResource(String code, String state);

    NaverAccessResponse getAccessToken(String code, String state);

    NaverResourceResponse getResource(String accessToken, String tokenType);

}
