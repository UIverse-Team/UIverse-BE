package com.jishop.member.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.dto.response.SocialUserInfo;
import com.jishop.member.dto.response.UserIdResponse;
import com.jishop.member.dto.response.UserResponse;
import com.jishop.member.repository.UserRepository;
import com.jishop.member.service.UserService;
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

    public FindUserResponse findUser(FindUserRequest request){
        User user = userRepository.findByPhone(request.phone())
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        return FindUserResponse.of(user);
    }


    // user id 들고오는 서비스 필요
    public UserIdResponse findUserId(EmailRequest request){
        User user = userRepository.findByLoginId(request)
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        return UserIdResponse.from(user.getId());
    }

}