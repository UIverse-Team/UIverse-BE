package com.jishop.member.service;

import com.jishop.member.domain.LoginType;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.SocialUserInfo;
import com.jishop.member.dto.response.UserIdResponse;
import com.jishop.member.dto.response.UserResponse;
import jakarta.servlet.http.HttpSession;

public interface UserService {

    Long processOAuthUser(SocialUserInfo socialUserInfo, LoginType provider);
    void signUp(SignUpFormRequest form);
    void emailcheck(Step1Request request);
    FindUserResponse findUser(FindUserRequest request);
    UserIdResponse findUserId(EmailRequest request);
}
