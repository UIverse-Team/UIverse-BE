package com.jishop.member.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.UserResponse;
import com.jishop.member.repository.UserRepository;
import com.jishop.member.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void signIn(SignInFormRequest form, HttpSession session) {
        User user = userRepository.findByLoginId(form.loginId())
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        if(!passwordEncoder.matches(form.password(), user.getPassword())) {
            throw new DomainException(ErrorType.USER_NOT_FOUND);
        }
        if(user.isDeleteStatus()) throw new DomainException(ErrorType.USER_NOT_FOUND);

        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(60 * 60);
    };


    @Override
    public String loginStr(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        return user.getName();
    }

    /**
     *  추후 변경 필요 user 필요
     * @param request
     */
    public void recoveryPW(RecoveryPWRequest request){
        User user = userRepository.findByLoginId(request.email())
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        String password = passwordEncoder.encode(request.password());
        user.updatePassword(password);
    }

    public boolean checkPW(RecoveryPWRequest request){
        User user = userRepository.findByLoginId(request.email())
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        return !passwordEncoder.matches(request.password(), user.getPassword());
    }

    public void updatePW(User user, UserNewPasswordRequest request){
        User persistUser = getPersistUser(user);

        if(passwordEncoder.matches(request.password(), persistUser.getPassword())){
            throw new DomainException(ErrorType.PASSWORD_EXISTS);
        }

        String password = passwordEncoder.encode(request.password());
        persistUser.updatePassword(password);
        updateUserCache(persistUser);
    }

    // todo: 회원 정보 조회
    public UserResponse getUser(User user){
        return UserResponse.from(user);
    }

    // todo: 회원 정보 수정 (이름, 전화번호)
    public void updateUserName(User user, UserNameRequest request) {
        User persistUser = getPersistUser(user);

        persistUser.updateName(request.name());
        updateUserCache(persistUser);
    }

    public void updatePhone(User user, UserPhoneRequest request) {
        User persistUser = getPersistUser(user);

        persistUser.updatePhone(request.phone());
        updateUserCache(persistUser);
    }

    public void deleteUser(User user) {
        User persistUser = getPersistUser(user);
        persistUser.delete();
    }

    public Long checkLogin(User user) {
        return user.getId();
    }

    public void updateAdSMSAgree(User user, UserAdSMSRequest request){
        User persistUser = getPersistUser(user);

        persistUser.updateAdSMSAgree(request.adSMSAgree());
        updateUserCache(persistUser);
    }

    public void updateAdEmailAgree(User user, UserAdEmailRequest request){
        User persistUser = getPersistUser(user);

        persistUser.updateAdEmailAgree(request.adEmailAgree());
        updateUserCache(persistUser);
    }

    private void updateUserCache(User user) {
        String cacheKey = "user::" + user.getId();
        // 캐시 업데이트 (기존 캐시 삭제 후 최신 정보로 재설정)
        redisTemplate.delete(cacheKey);
        redisTemplate.opsForValue().set(cacheKey, user, Duration.ofMinutes(30));
    }

    private User getPersistUser(User user){
        User persistUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        return persistUser;
    }

    public void logout(User user) {
        String cacheKey = "user::" + user.getId();
        redisTemplate.delete(cacheKey);
    }
}

