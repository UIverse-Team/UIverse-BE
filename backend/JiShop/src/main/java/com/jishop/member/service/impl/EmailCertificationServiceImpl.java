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
        String certificationCode = String.format("%06d", (int)(Math.random() * 1000000));
        EmailCertification certification = request.toEntity(token, certificationCode);

        repository.save(certification);

        String subject = "Ora 이메일 인증번호를 보내드립니다";

        String body = """
<html>
  <body style="margin: 0; padding: 0; font-family: 'Apple SD Gothic Neo', Arial, sans-serif; background-color: #ffffff;">
    <table width="100%%" cellpadding="0" cellspacing="0" style="padding: 40px 0;">
      <tr>
        <td align="center">
          <table width="600" cellpadding="0" cellspacing="0" style="border: 1px solid #FF4D00; border-radius: 10px; padding: 40px; box-shadow: 0 4px 12px rgba(0,0,0,0.05);">
            <tr>
              <td align="left">
                <img src="https://api.uiverse.shop/Ora.png" alt="Ora 로고" width="74" height="31" style="display:block;" />
                <p style="font-size: 14px; color: #FF4D00; font-weight: bold;">Ora의 보낸 인증 번호입니다.</p>
                
                <h2 style="color: #333333; margin-top: 30px;">이메일 주소를 인증해주세요</h2>
                <p style="font-size: 16px; color: #555555;">아래 인증번호를 입력하시면 인증이 완료됩니다.</p>

                <div style="margin: 30px 0; background-color: #FFF3ED; padding: 20px; border-radius: 5px;">
                  <p style="font-size: 32px; font-weight: bold; color: #FF4D00; text-align: center; letter-spacing: 4px;">%s</p>
                </div>

                <p style="font-size: 14px; color: #999999;">이 인증번호는 발송 시점으로부터 <strong>5분간 유효</strong>합니다.</p>
                <p style="font-size: 14px; color: #999999;">고객센터 문의는 Ora 홈페이지를 참고해주세요.</p>

                <hr style="margin-top: 30px; border: none; border-top: 1px solid #eee;" />
                <p style="font-size: 12px; color: #bbb;">© Ora | 무적 UIverse </p>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
  </body>
</html>
""".formatted(certificationCode);

        try {
            emailSender.send(request.email(), subject, body);  // 여기서 HTML 메일 전송됨
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
