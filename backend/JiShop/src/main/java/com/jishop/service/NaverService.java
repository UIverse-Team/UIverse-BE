package com.jishop.service;

import com.jishop.dto.NaverUserInfo;

public interface NaverService {

    NaverUserInfo authenticateUserWithNaver(String code, String state);
}
