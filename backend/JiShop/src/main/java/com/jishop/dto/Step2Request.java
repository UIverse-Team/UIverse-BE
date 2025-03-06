package com.jishop.dto;

import com.jishop.annotation.Password;

public record Step2Request(
        @Password
        String password
) {
}
