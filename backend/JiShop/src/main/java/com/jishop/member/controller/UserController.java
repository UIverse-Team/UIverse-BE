package com.jishop.member.controller;

import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.request.RecoveryPWRequest;
import com.jishop.member.dto.request.UserNameRequest;
import com.jishop.member.dto.request.UserPhoneRequest;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserController {

    FindUserResponse findUser(FindUserRequest request);
    ResponseEntity<String> recoverypw(RecoveryPWRequest request);
    UserResponse getUser();
    ResponseEntity<String> updateUserName(Long userId, UserNameRequest request);
    ResponseEntity<String> updatePhone(Long userId, UserPhoneRequest request);
}
