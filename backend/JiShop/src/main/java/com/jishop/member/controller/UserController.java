package com.jishop.member.controller;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "유저 서비스 API")
public interface UserController {

    @Operation(summary = "아이디 찾기")
    FindUserResponse findUser(FindUserRequest request);
    @Operation(summary = "현재 비번, 변경될 비번 동일 여부 체크")
    ResponseEntity<Boolean> checkPW(RecoveryPWRequest request);
    @Operation(summary = "비번 찾기 비밀번호 변경")
    ResponseEntity<String> recoveryPW(RecoveryPWRequest request);
    @Operation(summary = "마이페이지 비번 변경")
    ResponseEntity<String> updatePW(User user, UserNewPasswordRequest request);
    @Operation(summary = "회원 정보")
    UserResponse getUser(User user);
    @Operation(summary = "회원 이름 변경")
    ResponseEntity<String> updateUserName(User user, UserNameRequest request);
    @Operation(summary = "폰번호 변경")
    ResponseEntity<String> updatePhone(User user, UserPhoneRequest request);
    @Operation(summary = "회원 탈퇴")
    ResponseEntity<String> deleteUser(User user);
    @Operation(summary = "sms 수신 동의")
    void updateAdSMS( User user, UserAdSMSRequest request);
    @Operation(summary = "email 수신 동의")
    void updateAdEmail(User user, UserAdEmailRequest request);
}
