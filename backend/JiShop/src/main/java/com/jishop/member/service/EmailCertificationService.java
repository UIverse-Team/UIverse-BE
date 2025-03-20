package com.jishop.member.service;

import com.jishop.member.dto.request.EmailRequest;

public interface EmailCertificationService {

    String sendCertificationCodeForSignup(EmailRequest request);
    String sendCertificationCodeForPasswordReset(EmailRequest request);
    boolean certifyCode(String token, String code);
}
