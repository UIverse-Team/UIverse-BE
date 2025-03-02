package com.jishop.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.domain.User;
import com.jishop.dto.SignInFormRequest;
import com.jishop.dto.SignUpFormRequest;
import com.jishop.repository.UserRepository;
import com.jishop.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final HttpSession session;

    public void signUp(SignUpFormRequest form) {
        userRepository.save(form.toEntity());
    }

    public void signIn(SignInFormRequest form, HttpSession session) {
        User user = userRepository.findByLoginId(form.loginId())
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        // 추가사항
        // 비밀번호 검사 로직 추가
        // 로그인 완료시 Session 주기
        session.setAttribute("user", user);
        // 시간도 줘야하나?
        // Session redis에 저장하기
    };
}
