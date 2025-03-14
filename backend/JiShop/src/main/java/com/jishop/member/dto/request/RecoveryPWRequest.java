package com.jishop.member.dto.request;

import com.jishop.member.annotation.Password;

public record RecoveryPWRequest(
        @Password
        String password
) {
}
