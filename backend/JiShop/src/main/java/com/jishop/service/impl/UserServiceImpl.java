package com.jishop.service.impl;

import com.jishop.domain.LoginType;
import com.jishop.domain.User;
import com.jishop.dto.SocialUserInfo;
import com.jishop.repository.UserRepository;
import com.jishop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    public User processOAuthUser(SocialUserInfo socialUserInfo, LoginType provider) {
        // Check if user already exists with this socialId and provider
        return userRepository.findByLoginIdAndProvider(socialUserInfo.id(), provider)
                .orElseGet(() -> {
                    // If not, create a new user
                    User user = User.builder()
                            .loginId(socialUserInfo.email())
                            .name(socialUserInfo.name())
                            .password(null)
                            .provider(provider)
                            .build();
                    return userRepository.save(user);
                });
    }
}
