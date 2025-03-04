package com.jishop.repository;

import com.jishop.domain.EmailCertification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, String> {

    Optional<EmailCertification> findByTokenAndCertificationCode(String token, String certificationCode);
}
