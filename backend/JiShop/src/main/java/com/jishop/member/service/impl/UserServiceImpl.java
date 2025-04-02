package com.jishop.member.service.impl;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import com.jishop.member.dto.request.FindUserRequest;
import com.jishop.member.dto.request.SignUpFormRequest;
import com.jishop.member.dto.response.FindUserResponse;
import com.jishop.member.repository.UserRepository;
import com.jishop.member.service.OAuthProfile;
import com.jishop.member.service.UserService;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public User oauthLogin(OAuthProfile profile) {
        String loginId = profile.getEmail();
        LoginType provider = LoginType.valueOf(profile.getProvider().toUpperCase());

        Optional<User> existingUser = userRepository.findByLoginIdAndProvider(loginId, provider);

        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            // 네이버 특화 필드 추출
            String birthyear = null;
            String birthday = null;
            String gender = null;
            String phone = null;
            String birthDate = "";
            
            if(profile instanceof NaverProfile naverProfile) {
                birthyear = naverProfile.getBirthyear();
                birthday = naverProfile.getBirthday();
                birthDate = birthyear + "-" + birthday;
                gender = naverProfile.getGender();
                phone = naverProfile.getMobile();
            }

            User newUser = User.builder()
                    .loginId(loginId)
                    .password(null)
                    .name(profile.getName())
                    .provider(provider)
                    .birthDate(birthDate)
                    .gender(gender)
                    .phone(phone)
                    .ageAgreement(true)
                    .useAgreement(true)
                    .picAgreement(true)
                    .adAgreement(false)
                    .build();

            return userRepository.save(newUser);
        }
    }

    public void signUp(SignUpFormRequest request) {
        String password = passwordEncoder.encode(request.password());
        if(userRepository.findByLoginId(request.loginId()).isPresent()) {
            throw new DomainException(ErrorType.EMAIL_DUPLICATE);
        };
        SignUpFormRequest upFormRequest = request.withPassword(password);

        userRepository.save(upFormRequest.toEntity());
    }

    public FindUserResponse findUser(FindUserRequest request){
        User user = userRepository.findByPhone(request.phone())
                .orElseThrow(() -> new DomainException(ErrorType.USER_NOT_FOUND));
        if(user.isDeleteStatus()) throw new DomainException(ErrorType.USER_NOT_FOUND);

        return FindUserResponse.of(user);
    }
}