package com.jishop.member.service;

import com.jishop.member.domain.User;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.UserResponse;
import jakarta.servlet.http.HttpSession;

public interface AuthService {

    void signIn(SignInFormRequest form, HttpSession session);
    String loginStr(Long userId);
    void recoveryPW(RecoveryPWRequest request);
    boolean checkPW(RecoveryPWRequest request);
    void updatePW(User user, UserNewPasswordRequest request);
    UserResponse getUser(User user);
    void updateUserName(User user, UserNameRequest request);
    void updatePhone(User user, UserPhoneRequest request);
    void deleteUser(User user);
    Long checkLogin(User user);
    void updateAdSMSAgree(User user, UserAdSMSRequest request);
    void updateAdEmailAgree(User user, UserAdEmailRequest request);
}
