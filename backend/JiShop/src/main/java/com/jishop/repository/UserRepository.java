package com.jishop.repository;

import com.jishop.domain.LoginType;
import com.jishop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginIdAndProvider(String loginId, LoginType provider);

    Optional<User> findByLoginId(String loginId);
}
