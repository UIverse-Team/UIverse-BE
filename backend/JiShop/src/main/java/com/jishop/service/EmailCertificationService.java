package com.jishop.service;

import com.jishop.domain.EmailCertificationType;
import com.jishop.dto.EmailCertifyCodeRequest;
import com.jishop.dto.EmailRequest;

public interface EmailCertificationService {

    String sendCerificationCode(EmailRequest request);
    boolean certifyCode(String token, String request);
}
