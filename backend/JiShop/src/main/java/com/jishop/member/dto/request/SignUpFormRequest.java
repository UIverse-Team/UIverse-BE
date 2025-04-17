package com.jishop.member.dto.request;

import com.jishop.member.annotation.Password;
import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpFormRequest(
        @AssertTrue(message = "필수 사항입니다!!!")
        boolean ageAgreement,
        @AssertTrue(message = "필수 사항입니다!!!")
        boolean useAgreement,
        @AssertTrue(message = "필수 사항입니다!!!")
        boolean picAgreement,
        boolean adAgreement,
        @Email
        @NotBlank
        String loginId,
        @NotBlank
        @Password
        String password,
        @NotBlank
        String name,
        @NotBlank
        String birthDate,
        @NotBlank
        String gender,
        @NotBlank
        String phone,
        LoginType provider
){
    // 비밀번호 업데이트
    public SignUpFormRequest withPassword(String password){
        return new SignUpFormRequest(this.ageAgreement, this.useAgreement, this.picAgreement,
                this.adAgreement, this.loginId, password, this.name, this.birthDate, this.gender,
                this.phone, this.provider);
    }

    public User toEntity(){
        boolean adSMSAgree = this.adAgreement;
        boolean adEmailAgree = this.adAgreement;

        return new User(
                this.ageAgreement,
                this.useAgreement,
                this.picAgreement,
                this.adAgreement,
                adSMSAgree,
                adEmailAgree,
                this.loginId,
                this.password,
                this.name,
                this.birthDate,
                this.gender,
                this.phone,
                LoginType.LOCAL
        );
    }
}
