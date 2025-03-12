package com.jishop.member.dto;

import jakarta.validation.constraints.Email;


public record Step1Request(
        @Email
        String email
) {
}


