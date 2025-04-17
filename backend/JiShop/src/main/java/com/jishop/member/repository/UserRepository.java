package com.jishop.member.repository;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginIdAndProvider(String loginId, LoginType provider);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByPhone(String phone);

    boolean existsByLoginId(String loginId);

    default User findPersistUser(User user) {
        return findById(user.getId()).orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
    }
}
