package com.jishop.member.dto.request;

import com.jishop.member.domain.User;

public record UserAdEmailRequest(
        boolean adEmailAgree
) {
    public void update(User user){
        user.updateAdEmailAgree(adEmailAgree);
    }
}
