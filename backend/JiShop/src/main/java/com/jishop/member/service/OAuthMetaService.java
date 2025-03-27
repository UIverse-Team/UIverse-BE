package com.jishop.member.service;

import com.jishop.member.dto.response.OAuthMetaResponse;

public interface OAuthMetaService {

    OAuthMetaResponse getMeta(String provider);
}

