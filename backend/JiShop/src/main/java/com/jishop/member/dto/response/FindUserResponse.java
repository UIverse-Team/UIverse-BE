package com.jishop.member.dto.response;

import com.jishop.member.domain.User;

public record FindUserResponse(
        String loginId
) {
    public static FindUserResponse of(User user) {
        return new FindUserResponse(user.getLoginId());
    }
}
