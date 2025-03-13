package com.jishop.member.service;

import com.jishop.member.domain.LoginType;
import com.jishop.member.dto.SignInFormRequest;
import com.jishop.member.dto.SignUpFormRequest;
import com.jishop.member.dto.SocialUserInfo;
import com.jishop.member.dto.Step1Request;
import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.response.FindUserResponse;
import jakarta.servlet.http.HttpSession;

public interface UserService {

    Long processOAuthUser(SocialUserInfo socialUserInfo, LoginType provider);
    void signUp(SignUpFormRequest form);
    void signIn(SignInFormRequest form, HttpSession session);
    void emailcheck(Step1Request request);
    String loginStr(Long userId);
    FindUserResponse findUser(FindUserRequest request);
}
