package com.jishop.member.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import com.jishop.member.dto.SignInFormRequest;
import com.jishop.member.dto.SignUpFormRequest;
import com.jishop.member.dto.SocialUserInfo;
import com.jishop.member.dto.Step1Request;
import com.jishop.member.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long processOAuthUser(SocialUserInfo socialUserInfo, LoginType provider) {
        // 이미 아이디 존재시 회원가입이 아닌 로그인으로 바꿈
        userRepository.findByLoginIdAndProvider(socialUserInfo.id(), provider)
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

        User user = userRepository.findByLoginIdAndProvider(socialUserInfo.id(), provider).orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
        Long userId = user.getId();
        return userId;
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

        if(!passwordEncoder.matches(form.password(), user.getPassword())) {
            throw new DomainException(ErrorType.USER_NOT_FOUND);
        }

        session.setAttribute("userId", user.getId());
    };

    public String loginStr(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
        return user.getName();
    }
}
