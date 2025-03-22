package com.jishop.member.controller;

import com.jishop.member.domain.User;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {

    FindUserResponse findUser(FindUserRequest request);
    ResponseEntity<String> recoveryPW(RecoveryPWRequest request);
    ResponseEntity<String> updatePW(User user, UserNewPasswordRequest request);
    UserResponse getUser(User user);
    ResponseEntity<String> updateUserName(User user, UserNameRequest request);
    ResponseEntity<String> updatePhone(User user, UserPhoneRequest request);
    ResponseEntity<String> deleteUser(User user);
}
