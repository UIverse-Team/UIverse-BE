package com.jishop.member.service;

import com.jishop.member.dto.request.RecoveryPWRequest;
import com.jishop.member.dto.request.SignInFormRequest;
import com.jishop.member.dto.request.UserNameRequest;
import com.jishop.member.dto.request.UserPhoneRequest;
import com.jishop.member.dto.response.UserResponse;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    void signIn(SignInFormRequest form, HttpSession session);
    String loginStr(Long userId);
    void recoveryPW(Long userId, RecoveryPWRequest request);
    UserResponse getUser(Long userId);
    void updateUserName(Long userId, UserNameRequest request);
    void updatePhone(Long userId, UserPhoneRequest request);
}
