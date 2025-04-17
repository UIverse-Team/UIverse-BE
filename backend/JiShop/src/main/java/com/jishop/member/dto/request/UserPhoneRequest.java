package com.jishop.member.dto.request;

import com.jishop.member.domain.User;
import jakarta.validation.constraints.NotBlank;

public record UserPhoneRequest(
        @NotBlank
        String phone
) {
        public void update(User user){
                user.updatePhone(phone);
        }
}
