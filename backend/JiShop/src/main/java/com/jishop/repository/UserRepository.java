package com.jishop.repository;

import com.jishop.domain.LoginType;
import com.jishop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginIdAndProvider(String loginId, LoginType provider);
}
