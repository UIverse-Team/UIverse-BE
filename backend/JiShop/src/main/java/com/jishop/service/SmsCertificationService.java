package com.jishop.service;

import com.jishop.dto.SmsRequest;

public interface SmsCertificationService {

    void sendSms(String to, String message);
    boolean verifyCode(String token, String code);
    String sendVerificationCode(SmsRequest request);
}
