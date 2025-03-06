package com.jishop.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.domain.LoginType;
import com.jishop.domain.User;
import com.jishop.dto.SignInFormRequest;
import com.jishop.dto.SignUpFormRequest;
import com.jishop.dto.SocialUserInfo;
import com.jishop.dto.Step1Request;
import com.jishop.repository.UserRepository;
import com.jishop.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final HttpSession session;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User processOAuthUser(SocialUserInfo socialUserInfo, LoginType provider) {
        // Check if user already exists with this socialId and provider
        return userRepository.findByLoginIdAndProvider(socialUserInfo.id(), provider)
                .orElseGet(() -> {
                    // If not, create a new user
                    User user = User.builder()
                            .loginId(socialUserInfo.id())
                            .name(socialUserInfo.name())
                            .password(null)
                            .provider(provider)
                            .build();
                    return userRepository.save(user);
                });
    }

    public void emailcheck(Step1Request request){
        if(userRepository.findByLoginId(request.email()).isPresent()){
            throw new DomainException(ErrorType.EMAIL_DUPLICATE);
        }
    }

    public void signUp(SignUpFormRequest form) {
        userRepository.save(form.toEntity());
    }

    public void signIn(SignInFormRequest form, HttpSession session) {
        User user = userRepository.findByLoginId(form.loginId())
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
        // 추가사항
        // 비밀번호 검사 로직
        if(!passwordEncoder.matches(form.password(), user.getPassword())) {
            throw new DomainException(ErrorType.USER_NOT_FOUND);
        }

        // 로그인 완료시 Session 추가
        session.setAttribute("user", user);
        // 시간도 줘야하나?
        // Session redis에 저장하기
    };
}
