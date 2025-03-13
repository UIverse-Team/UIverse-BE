package com.jishop.member.controller;

import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.request.RecoveryPWRequest;
import com.jishop.member.dto.response.FindUserResponse;
import org.springframework.http.ResponseEntity;

public interface UserController {

    FindUserResponse findUser(FindUserRequest request);
    ResponseEntity<String> recoverypw(RecoveryPWRequest request);
}
