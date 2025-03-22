package com.jishop.member.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.*;
import com.jishop.member.dto.response.*;
import com.jishop.member.repository.UserRepository;
import com.jishop.member.service.OAuthProfile;
import com.jishop.member.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User oauthLogin(OAuthProfile profile) {
        String loginId = profile.getProviderId();
        LoginType provider = LoginType.valueOf(profile.getProvider().toUpperCase());

        Optional<User> existingUser = userRepository.findByLoginIdAndProvider(loginId, provider);
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            User newUser = User.builder()
                    .loginId(loginId)
                    .password(null)
                    .name(profile.getName())
                    .provider(provider)
                    .birthDate(null)
                    .gender(null)
                    .phone(null)
                    .ageAgreement(true)
                    .useAgreement(true)
                    .picAgreement(true)
                    .adAgreement(false)
                    .build();
            return userRepository.save(newUser);
        }
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

        if(user.isDeleteStatus()) throw new DomainException(ErrorType.USER_NOT_FOUND);

        return FindUserResponse.of(user);
    }

    // user id 들고오는 서비스 필요
    public UserIdResponse findUserId(EmailRequest request){
        User user = userRepository.findByLoginId(request)
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));

        return UserIdResponse.from(user.getId());
    }
}