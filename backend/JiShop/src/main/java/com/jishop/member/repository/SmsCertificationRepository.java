package com.jishop.member.repository;

import com.jishop.member.domain.SmsCertification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SmsCertificationRepository extends JpaRepository<SmsCertification, Long> {

    Optional<SmsCertification> findByTokenAndCode(String token, String code);
    @Modifying
    @Transactional
    @Query("delete from SmsCertification e where e.expiresAt < :now")
    void deleteByExpiresAtBefore(@Param("now") LocalDateTime now);
}
