package com.jishop.member.dto.request;

import com.jishop.member.domain.User;
import jakarta.validation.constraints.NotBlank;

public record UserNameRequest(
        @NotBlank
        String name
) {
        public void update(User user){
                user.updateName(name);
        }
}
