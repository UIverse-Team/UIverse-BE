package com.jishop.member.dto.request;

import com.jishop.member.annotation.Password;
import jakarta.validation.constraints.NotBlank;

public record UserNewPasswordRequest(
        @NotBlank(message = "비밀번호를 입력해주세요!")
        @Password
        String password
) {
}
