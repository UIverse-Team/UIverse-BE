package com.jishop.member.annotation;

import com.jishop.member.domain.User;
import com.jishop.member.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) return null;

        HttpSession session = request.getSession(false);
        if (session == null) return null;

        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) return null;

        Long userId;

        try {
            userId = Long.parseLong(userIdObj.toString());
        } catch (NumberFormatException e) {
            return null;
        }

        // 캐시에서 조회
        String cacheKey = "user::" + userId;
        User cachedUser = (User) redisTemplate.opsForValue().get(cacheKey);

        if (cachedUser != null) {
            return cachedUser;
        }

        return userRepository.findById(userId).map(user -> {
            redisTemplate.opsForValue().set(cacheKey, user, Duration.ofMinutes(30));
            return user;
        }).orElse(null);
    }
}
