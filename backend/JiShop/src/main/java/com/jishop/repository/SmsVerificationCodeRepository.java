package com.jishop.repository;

import com.jishop.domain.SmsVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsVerificationCodeRepository extends JpaRepository<SmsVerificationCode, Long> {

    Optional<SmsVerificationCode> findByTokenAndCode(String token, String code);
}
