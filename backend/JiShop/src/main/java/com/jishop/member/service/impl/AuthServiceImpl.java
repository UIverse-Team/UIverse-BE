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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

        if(passwordEncoder.matches(request.password(), user.getPassword())){
            throw new DomainException(ErrorType.PASSWORD_EXISTS);
        }

        String password = passwordEncoder.encode(request.password());
        user.updatePassword(password);
    }

    public void updatePW(User user, UserNewPasswordRequest request){
        if(passwordEncoder.matches(request.password(), user.getPassword())){
            throw new DomainException(ErrorType.PASSWORD_EXISTS);
        }

        String password = passwordEncoder.encode(request.password());
        user.updatePassword(password);
    }

    // todo: 회원 정보 조회
    public UserResponse getUser(User user){
        return UserResponse.from(user);
    }

    // todo: 회원 정보 수정 (이름, 전화번호)
    public void updateUserName(User user, UserNameRequest request) {
        user.updateName(request.name());
    }

    public void updatePhone(User user, UserPhoneRequest request) {
        user.updatePhone(request.phone());
    }

    public void deleteUser(User user) {
        user.delete();
    }

    public Long checkLogin(User user) {
        return user.getId();
    }
}

