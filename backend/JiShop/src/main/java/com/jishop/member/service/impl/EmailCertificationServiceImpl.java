package com.jishop.member.service.impl;

import com.amazonaws.services.simpleemail.model.AmazonSimpleEmailServiceException;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.EmailCertification;
import com.jishop.member.dto.request.EmailRequest;
import com.jishop.member.repository.EmailCertificationRepository;
import com.jishop.member.repository.UserRepository;
import com.jishop.member.service.EmailCertificationService;
import com.jishop.member.service.EmailSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailCertificationServiceImpl implements EmailCertificationService {

    private final EmailSender emailSender;
    private final EmailCertificationRepository repository;
    private final UserRepository userRepository;

    @Override
    public String sendCertificationCodeForSignup(EmailRequest request) {
        if (userRepository.existsByLoginId(request.email())) {
            throw new DomainException(ErrorType.EMAIL_DUPLICATE);
        }
        return sendCertificationCode(request);
    }

    @Override
    public String sendCertificationCodeForPasswordReset(EmailRequest request) {
        if (!userRepository.existsByLoginId(request.email())) {
            throw new DomainException(ErrorType.USER_NOT_FOUND);
        }
        return sendCertificationCode(request);
    }

    private String sendCertificationCode(EmailRequest request) {
        String token = UUID.randomUUID().toString();
        String certificationCode = String.format("%06d", (int)(Math.random()*1000000));
        EmailCertification certification = request.toEntity(token, certificationCode);

        repository.save(certification);
        String subject = "인증 코드";
        String body = "인증 코드는 :" + certificationCode + "입니다!";

        try {
            emailSender.send(request.email(), subject, body);
        } catch (AmazonSimpleEmailServiceException e) {
            e.printStackTrace();
            throw new DomainException(ErrorType.EMAIL_SEND_FAILURE);
        }

        return token;
    }

    // 인증코드가 유효한건지 검증
    @Override
    public boolean certifyCode(String token, String certificationCode) {
        EmailCertification certification = repository.findByTokenAndCertificationCode(token, certificationCode)
                        .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
        // 인증 시도할 경우 코드 삭제
        repository.delete(certification);

        return certification.getExpiresAt().isAfter(LocalDateTime.now());
    }



    // 인증 시도 안한 경우의 인증 코드 -> 만료된 인증코드를 매 10분마다 자동으로 삭제(스케줄링 기능 사용)
    @Scheduled(fixedRate = 600000)
    public void cleanupExpiredCodes() {
        LocalDateTime now = LocalDateTime.now();
        log.info("start", now);
        repository.deleteByExpiresAtBefore(now);
        log.info("finish", LocalDateTime.now());
    }
}
