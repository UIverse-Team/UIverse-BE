package com.jishop.member.dto.request;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public record SignUpFormRequest(
        boolean ageAgreement,
        boolean useAgreement,
        boolean picAgreement,
        boolean adAgreement,
        String loginId,
        String password,
        String name,
        String birthDate,
        String gender,
        String phone,
        LoginType provider
){
    // 초기 생성 팩토리 메서드
    public static SignUpFormRequest of(boolean ageAgreement, boolean useAgreement, boolean picAgreement, boolean adAgreement){
        return new SignUpFormRequest(ageAgreement, useAgreement, picAgreement, adAgreement, null, null, null, null, null, null, LoginType.LOCAL);
    }

    public SignUpFormRequest withEmail(String email) {
        return new SignUpFormRequest(this.ageAgreement, this.useAgreement, this.picAgreement, this.adAgreement, email, null, null, null, null,null, this.provider);
    }

    // 비밀번호 업데이트
    public SignUpFormRequest withPassword(String password){
        return new SignUpFormRequest(this.ageAgreement, this.useAgreement, this.picAgreement, this.adAgreement, this.loginId, password, null,null,null,null, this.provider);
    }

    // 유저 정보 업데이트
    public SignUpFormRequest withInformation(String name, String birthDate, String gender, String phone){
        return new SignUpFormRequest(this.ageAgreement, this.useAgreement, this.picAgreement, this.adAgreement, this.loginId, password, name, birthDate, gender, phone, this.provider);
    }

    public User toEntity(){
        return new User(
                this.ageAgreement,
                this.useAgreement,
                this.picAgreement,
                this.adAgreement,
                this.loginId,
                this.password,
                this.name,
                this.birthDate,
                this.gender,
                this.phone,
                this.provider
        );
    }
}
