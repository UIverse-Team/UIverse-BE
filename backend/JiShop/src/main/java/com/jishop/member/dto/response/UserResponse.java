package com.jishop.member.dto.response;

import com.jishop.member.domain.User;

public record UserResponse(
        String name,
        String phone,
        String email
) {
    // todo: 나중에 로컬회원과 소셜회원 구별해서 로그인 아이디 내려주기
    public static UserResponse from(User user) {
        return new UserResponse(user.getName(), user.getPhone(), user.getLoginId());
    }
}
