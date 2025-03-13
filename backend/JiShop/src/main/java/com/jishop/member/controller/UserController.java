package com.jishop.member.controller;

import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.response.FindUserResponse;

public interface UserController {

    FindUserResponse findUser(FindUserRequest request);

}
