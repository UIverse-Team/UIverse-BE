package com.jishop.service.impl;

import com.jishop.domain.EmailCertification;
import com.jishop.dto.EmailRequest;
import com.jishop.repository.EmailCertificationRepository;
import com.jishop.service.EmailCertificationService;
import com.jishop.service.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailCertificationServiceImpl implements EmailCertificationService {

    private final EmailCertificationRepository repository;
    private final EmailSender emailSender;

    @Override
    public String sendCerificationCode(EmailRequest request) {
        // 토큰 생성
        String token = UUID.randomUUID().toString();
        // 인증 코드 6자리 난수 생성
        String certificationCode = String.format("%06d", (int)(Math.random()*1000000));
        EmailCertification certification = request.toEntity(token, certificationCode);
        repository.save(certification);
        String subject = "인증 코드";
        String body = "인증 코드는 :" + certificationCode + "입니다!";
        emailSender.send(request.email(), subject, body);

        return token;
    }

    // 인증코드가 유효한건지 검증
    @Override
    public boolean certifyCode(String token, String certificationCode) {
        Optional<EmailCertification> code = repository.findByTokenAndCertificationCode(token, certificationCode);

        // 인증 시도할 경우 -> 인증 성공시 삭제, 시도한 코드가 만료된 코드일시 삭제
        if(code.isPresent()){
            EmailCertification certification = code.get();
            if(certification.getExpiresAt().isAfter(LocalDateTime.now())){
                repository.delete(certification);

                return true;
            } else {
                repository.delete(certification);
            }
        }
        return false;
    }

    // 인증 시도 안한 경우의 인증 코드 -> 만료된 인증코드를 매 1분마다 자동으로 삭제(스케줄링 기능 사용)
    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredCodes() {
        LocalDateTime now = LocalDateTime.now();
        repository.findAll().stream()
                .filter(certification -> certification.getExpiresAt().isBefore(now))
                .forEach(repository::delete);
    }
}
