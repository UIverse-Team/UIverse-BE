package com.jishop.member.controller.impl;

import com.jishop.member.controller.UserController;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.UserIdResponse;
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
    public FindUserResponse findUser(@RequestBody @Validated FindUserRequest request){
        return userService.findUser(request);
    }

    @PostMapping("/finduser")
    public UserIdResponse emailUser(@RequestBody @Validated EmailRequest request){
        return userService.findUserId(request);
    }

    @PatchMapping("/recoverypw")
    public ResponseEntity<String> recoverypw(@RequestBody @Validated RecoveryPWRequest request){
        Long userid = 1l;
        authService.recoveryPW(userid, request);

        return ResponseEntity.ok().body("비밀번호 뱐경완료!");
    }

    /** todo: 회원 정보 조회
     *  추후 수정 필요
     */
    @GetMapping()
    public UserResponse getUser(){
        long user = 1L;

        return authService.getUser(user);
    }

    /**
     * todo: 회원 정보 수정
     * 이름, 전화번호
     */
    @PatchMapping("/name")
    public ResponseEntity<String> updateUserName(Long userId, @RequestBody @Validated UserNameRequest request) {
        long user = 1L;
        authService.updateUserName(user, request);

        return ResponseEntity.ok("이름 변경 완료!");
    }

    @PatchMapping("/phone")
    public ResponseEntity<String> updatePhone(Long userId, @RequestBody @Validated UserPhoneRequest request) {
        long user = 1L;
        authService.updatePhone(user, request);

        return ResponseEntity.ok("번호 변경 완료!");
    }

    // todo: 회원 주소 추가?
}
