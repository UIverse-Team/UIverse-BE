package com.jishop.member.repository;

import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import com.jishop.member.dto.EmailRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginIdAndProvider(String loginId, LoginType provider);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByPhone(String phone);
    Optional<User> findByLoginId(EmailRequest request);
}
