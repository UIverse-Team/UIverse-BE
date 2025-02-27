package com.jishop.service;

import com.jishop.dto.KakaoUserInfo;

public interface KakaoService {

    KakaoUserInfo authenticateUserWithKakao(String code);
}
