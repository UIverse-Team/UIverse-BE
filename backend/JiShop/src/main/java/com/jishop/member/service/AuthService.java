package com.jishop.member.service;

import com.jishop.member.domain.User;
import com.jishop.member.dto.request.RecoveryPWRequest;
import com.jishop.member.dto.request.SignInFormRequest;
import com.jishop.member.dto.request.UserNameRequest;
import com.jishop.member.dto.request.UserPhoneRequest;
import com.jishop.member.dto.response.UserResponse;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    void signIn(SignInFormRequest form, HttpSession session);
    String loginStr(Long userId);
    void recoveryPW(User user, RecoveryPWRequest request);
    UserResponse getUser(User user);
    void updateUserName(User user, UserNameRequest request);
    void updatePhone(User user, UserPhoneRequest request);
    void deleteUser(User user);
}
