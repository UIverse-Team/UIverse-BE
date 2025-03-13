package com.jishop.member.dto.request;

import com.jishop.member.annotation.Password;

public record Step2Request(
        @Password
        String password
) {
}
