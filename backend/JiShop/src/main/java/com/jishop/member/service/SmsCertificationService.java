package com.jishop.member.service;

import com.jishop.member.dto.SmsRequest;

public interface SmsCertificationService {

    void sendSms(String to, String message);
    boolean verifyCode(String token, String code);
    String sendVerificationCode(SmsRequest request);
}
