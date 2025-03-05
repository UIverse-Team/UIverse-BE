package com.jishop.service.impl;

import com.jishop.domain.SmsVerificationCode;
import com.jishop.repository.SmsVerificationCodeRepository;
import com.jishop.service.SmsService;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final SmsVerificationCodeRepository smsVerificationCodeRepository;

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
    public String sendVerificationCode(String phoneNumber) {
        String code = generateCode();
        String token = generateToken();

        SmsVerificationCode smsVerificationCode = SmsVerificationCode.builder()
                .phonenumber(phoneNumber)
                .token(token)
                .code(code)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .build();

        smsVerificationCodeRepository.save(smsVerificationCode);

        sendSms(phoneNumber, "[UIverse]인증번호는 : " + code + " 입니다!");

        return token;
    }

    @Override
    public boolean verifyCode(String token, String code) {
        return smsVerificationCodeRepository.findByTokenAndCode(token, code)
                .filter(verificationCode -> !verificationCode.isExpired())
                .map(verificationCode -> {
                    smsVerificationCodeRepository.delete(verificationCode);

                    return true;
                })
                .orElse(false);
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
        params.put("app_version", "JiShop App 1.0");

        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            e.printStackTrace();
            throw new RuntimeException("SMS 전송 실패: " + e.getMessage());
        }
    }
}
