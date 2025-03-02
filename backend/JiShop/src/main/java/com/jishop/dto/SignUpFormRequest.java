package com.jishop.dto;

import com.jishop.domain.LoginType;
import com.jishop.domain.User;

public record SignUpFormRequest(
        String loginId,
        String password,
        LoginType provider
) {
    // 초기 생성 팩토리 메서드
    public static SignUpFormRequest of(String email){
        return new SignUpFormRequest(email, null, LoginType.LOCAL);
    }

    // 비밀번호 업데이트
    public SignUpFormRequest withPassword(String password){
        return new SignUpFormRequest(this.loginId, password, this.provider);
    }

    public User toEntity(){
        return new User(
                this.loginId,
                this.password,
                this.provider
        );
    }
}
