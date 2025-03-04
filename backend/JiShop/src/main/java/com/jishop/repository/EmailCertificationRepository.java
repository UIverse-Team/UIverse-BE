package com.jishop.repository;

import com.jishop.domain.EmailCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailCertificationRepository extends JpaRepository<EmailCertification, String> {

    Optional<EmailCertification> findByTokenAndCertificationCode(String token, String certificationCode);
    @Modifying
    @Query("delete from EmailCertification e where e.expiresAt < :now")
    void deleteByExpiresAtBefore(@Param("now") LocalDateTime now);
}
