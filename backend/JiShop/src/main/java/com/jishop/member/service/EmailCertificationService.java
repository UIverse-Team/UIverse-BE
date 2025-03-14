package com.jishop.member.service;

import com.jishop.member.dto.request.EmailRequest;

public interface EmailCertificationService {

    String sendCerificationCode(EmailRequest request);
    boolean certifyCode(String token, String request);
}
