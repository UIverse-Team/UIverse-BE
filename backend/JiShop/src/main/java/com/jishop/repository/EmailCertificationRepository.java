package com.jishop.repository;

import com.jishop.domain.EmailCertification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailCertificationRepository extends JpaRepository<EmailCertification, String> {

    Optional<EmailCertification> findByTokenAndCertificationCode(String token, String certificationCode);
    @Modifying
    @Transactional
    @Query("delete from EmailCertification e where e.expiresAt < :now")
    void deleteByExpiresAtBefore(@Param("now") LocalDateTime now);
}
