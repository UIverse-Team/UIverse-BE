package com.jishop.service;

import com.jishop.member.dto.EmailRequest;

public interface EmailCertificationService {

    String sendCerificationCode(EmailRequest request);
    boolean certifyCode(String token, String request);
}
