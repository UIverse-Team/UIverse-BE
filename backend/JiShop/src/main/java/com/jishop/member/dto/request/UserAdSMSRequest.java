package com.jishop.member.dto.request;

import com.jishop.member.domain.User;

public record UserAdSMSRequest(
        boolean adSMSAgree
) {
    public void update(User user){
        user.updateAdSMSAgree(adSMSAgree);
    }
}
