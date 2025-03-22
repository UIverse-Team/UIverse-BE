package com.jishop.member.service;

import com.jishop.member.domain.User;
import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.request.SignUpFormRequest;
import com.jishop.member.dto.request.Step1Request;
import com.jishop.member.dto.response.FindUserResponse;

public interface UserService {

    User oauthLogin(OAuthProfile profile);
    void signUp(SignUpFormRequest form);
    void emailcheck(Step1Request request);
    FindUserResponse findUser(FindUserRequest request);
}
