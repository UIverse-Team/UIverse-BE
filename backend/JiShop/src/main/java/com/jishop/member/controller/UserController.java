package com.jishop.member.controller;

import com.jishop.member.domain.User;
import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.request.RecoveryPWRequest;
import com.jishop.member.dto.request.UserNameRequest;
import com.jishop.member.dto.request.UserPhoneRequest;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserController {

    FindUserResponse findUser(FindUserRequest request);
    ResponseEntity<String> recoverypw(User user,RecoveryPWRequest request);
    UserResponse getUser(User user);
    ResponseEntity<String> updateUserName(User user, UserNameRequest request);
    ResponseEntity<String> updatePhone(User user, UserPhoneRequest request);
    ResponseEntity<String> deleteUser(User user);
}
