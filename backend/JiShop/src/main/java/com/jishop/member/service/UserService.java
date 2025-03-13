package com.jishop.member.service;

import com.jishop.member.domain.LoginType;
import com.jishop.member.dto.*;
import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.request.RecoveryPWRequest;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.UserIdResponse;
import jakarta.servlet.http.HttpSession;

public interface UserService {

    Long processOAuthUser(SocialUserInfo socialUserInfo, LoginType provider);
    void signUp(SignUpFormRequest form);
    void signIn(SignInFormRequest form, HttpSession session);
    void emailcheck(Step1Request request);
    String loginStr(Long userId);
    FindUserResponse findUser(FindUserRequest request);
    UserIdResponse findUserId(EmailRequest request);
    void recoveryPW(Long userId, RecoveryPWRequest request);
}
