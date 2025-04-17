package com.jishop.member.controller.impl;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.controller.UserController;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.UserResponse;
import com.jishop.member.service.AuthService;
import com.jishop.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/recoveryid")
    public FindUserResponse findUser(@RequestBody @Validated FindUserRequest request) {
        return userService.findUser(request);
    }

    @PostMapping("/checkpw")
    public ResponseEntity<Boolean> checkPW(@RequestBody @Validated RecoveryPWRequest request) {
        if(!authService.checkPW(request)) return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @PatchMapping("/recoverypw")
    public ResponseEntity<String> recoveryPW(@RequestBody @Validated RecoveryPWRequest request) {
        authService.recoveryPW(request);

        return ResponseEntity.ok().body("비밀번호 뱐경완료!");
    }

    @PatchMapping("/password")
    public ResponseEntity<String> updatePW(@CurrentUser User user,
                                           @RequestBody @Validated UserNewPasswordRequest request) {
        authService.updatePW(user, request);

        return ResponseEntity.ok().body("비밀번호 뱐경완료!");
    }

    /**
     * todo: 회원 정보 조회 -> 더 많은 정보
     *  추후 수정 필요
     */
    @GetMapping()
    public UserResponse getUser(@CurrentUser User user) {
        return authService.getUser(user);
    }

    /**
     * todo: 회원 정보 수정
     * 이름, 전화번호
     */
    @PatchMapping("/name")
    public ResponseEntity<String> updateUserName(@CurrentUser User user,
                                                 @RequestBody @Validated UserNameRequest request) {
        authService.updateUserName(user, request);

        return ResponseEntity.ok("이름 변경 완료!");
    }

    @PatchMapping("/phone")
    public ResponseEntity<String> updatePhone(@CurrentUser User user,
                                              @RequestBody @Validated UserPhoneRequest request) {
        authService.updatePhone(user, request);

        return ResponseEntity.ok("번호 변경 완료!");
    }

    @PostMapping("/deleteId")
    public ResponseEntity<String> deleteUser(@CurrentUser User user) {
        authService.deleteUser(user);
        return ResponseEntity.ok("탈퇴 처리 완료!");
    }

    @PatchMapping("/adSMS")
    public void updateAdSMS(@CurrentUser User user,
                                            @RequestBody @Validated UserAdSMSRequest request) {
        authService.updateAdSMSAgree(user, request);
    }

    @PatchMapping("/adEmail")
    public void updateAdEmail(@CurrentUser User user,
                              @RequestBody @Validated UserAdEmailRequest request) {
        authService.updateAdEmailAgree(user, request);
    }
}
