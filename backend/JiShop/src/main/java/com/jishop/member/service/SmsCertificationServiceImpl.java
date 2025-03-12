package com.jishop.member.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.SmsCertification;
import com.jishop.member.dto.SmsRequest;
import com.jishop.member.repository.SmsCertificationRepository;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SmsCertificationServiceImpl implements SmsCertificationService {

    private final SmsCertificationRepository smsVerificationCodeRepository;

    @Value("${coolSms.apikey}")
    private String apiKey;

    @Value("${coolSms.secretkey}")
    private String apiSecret;

    @Value("${coolSms.sender-phone}")
    private String fromPhoneNumber;

    private Message coolsms;

    @PostConstruct
    public void initCoolSms() {
        this.coolsms = new Message(apiKey, apiSecret);
    }

    @Override
    public String sendVerificationCode(SmsRequest request) {
        String code = generateCode();
        String token = generateToken();

        SmsCertification smsVerificationCode = request.toEntity(token, code);

        smsVerificationCodeRepository.save(smsVerificationCode);

        sendSms(request.phoneNumber(), "[UIverse]인증번호는 : " + code + " 입니다!");

        return token;
    }

    @Override
    public boolean verifyCode(String token, String code) {
        SmsCertification verificationCode = smsVerificationCodeRepository.findByTokenAndCode(token, code)
                .orElseThrow(() -> new DomainException(ErrorType.TOKEN_NOT_FOUND));

        if (!verificationCode.isExpired()) {
            smsVerificationCodeRepository.delete(verificationCode);

            return true;
        }
        return false;
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void sendSms(String to, String message) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", to);
        params.put("from", fromPhoneNumber);
        params.put("type", "SMS");
        params.put("text", message);
        params.put(
                "app_version", "JiShop App 1.0");

        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            e.printStackTrace();
            throw new DomainException(ErrorType.SMS_SEND_FAILURE);
        }
    }
}
