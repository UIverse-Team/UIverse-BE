package com.jishop.service;

import com.jishop.dto.GoogleUserInfo;

public interface GoogleService {
    GoogleUserInfo authenticateUserWithGoogle(String code);
}
