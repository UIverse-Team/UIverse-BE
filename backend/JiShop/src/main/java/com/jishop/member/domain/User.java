package com.jishop.member.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements Serializable {

    @Column(nullable = false, name = "login_id")
    private String loginId;         // 회원 이메일, 소셜 로그인시 id 저장
    private String password;        // 비밀번호, null 저장
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginType provider;     // 로그인 타입 저장

    // 성별, 연령, 휴대전화 번호 추가 예정
    private String birthDate;
    private String gender;
    private String phone;

    private boolean ageAgreement;
    private boolean useAgreement;
    private boolean picAgreement;
    private boolean adAgreement;

    private boolean adSMSAgree;
    private boolean adEmailAgree;

    //소셜 회원가입
    @Builder
    public User(boolean ageAgreement, boolean useAgreement, boolean picAgreement, boolean adAgreement,
                boolean adSMSAgree, boolean adEmailAgree, String loginId, String password,
                String name, String birthDate, String gender, String phone, LoginType provider) {
        this.ageAgreement = ageAgreement;
        this.useAgreement = useAgreement;
        this.picAgreement = picAgreement;
        this.adAgreement = adAgreement;
        this.adSMSAgree = adSMSAgree;
        this.adEmailAgree = adEmailAgree;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.provider = provider;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateAdSMSAgree(boolean adSMSAgree) {
        this.adSMSAgree = adSMSAgree;
    }

    public void updateAdEmailAgree(boolean adEmailAgree) {
        this.adEmailAgree = adEmailAgree;
    }
}
