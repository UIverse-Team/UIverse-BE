package com.jishop.service;

import com.jishop.domain.LoginType;
import com.jishop.domain.User;
import com.jishop.dto.SignInFormRequest;
import com.jishop.dto.SignUpFormRequest;
import com.jishop.dto.SocialUserInfo;
import com.jishop.dto.Step1Request;
import jakarta.servlet.http.HttpSession;

public interface UserService {

    User processOAuthUser(SocialUserInfo socialUserInfo, LoginType provider);
    void signUp(SignUpFormRequest form);
    void signIn(SignInFormRequest form, HttpSession session);
    void emailcheck(Step1Request request);
    String generateWelcomeMessage(User user);
}
