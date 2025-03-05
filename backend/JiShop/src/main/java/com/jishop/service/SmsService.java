package com.jishop.service;

public interface SmsService {

    void sendSms(String to, String message);
    boolean verifyCode(String token, String code);
    String sendVerificationCode(String phoneNumber);
}
