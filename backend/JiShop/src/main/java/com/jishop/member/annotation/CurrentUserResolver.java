package com.jishop.member.annotation;

import com.jishop.member.domain.User;
import com.jishop.member.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    private final HttpSession session;
    private final UserRepository userRepository;

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
        if (request != null) {
            // HttpServletRequest로 인해서 직접 redis를 조회하는 코드 없는 대신 Spring Session이 HttpSession인터페이스를 통해 자동으로 Redis에 저장된 세션을 처리해줌 ㄷㄷ 굳
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object userIdObj = session.getAttribute("userId");
                if (userIdObj != null) {
                    Long userId;
                    if (userIdObj instanceof Integer) {
                        userId = ((Integer) userIdObj).longValue();
                    } else if (userIdObj instanceof Long) {
                        userId = (Long) userIdObj;
                    } else {
                        throw new IllegalStateException("Unexpected userId type: " + userIdObj.getClass());
                    }
                    return userRepository.findById(userId).orElse(null);
                }
            }
        }
        return null;
    }
}
