package com.jishop.member.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

public class CsrfFilter extends OncePerRequestFilter {

    private static final Set<String> SAFE_METHODS = new HashSet<>(List.of("GET"));
    private static final Set<String> CSRF_EXEMPT_PATHS = new HashSet<>(
            Arrays.asList("/auth/signin", "/signup", "/auth/request-password-reset"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 안전한 HTTP 메서드는 CSRF 검사 제외
        if (SAFE_METHODS.contains(request.getMethod())) {
            // 세션이 있고 CSRF 토큰이 없으면 토큰 생성
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("CSRF_TOKEN") == null) {
                generateCsrfToken(session, response);
            }
            filterChain.doFilter(request, response);
            return;
        }

        // CSRF 제외 경로 확인
        String requestUri = request.getRequestURI();
        if (CSRF_EXEMPT_PATHS.stream().anyMatch(requestUri::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 세션 및 CSRF 토큰 확인
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "인증되지 않은 요청입니다");
            return;
        }

        String sessionToken = (String) session.getAttribute("CSRF_TOKEN");
        String requestToken = request.getHeader("X-CSRF-TOKEN");

        // 쿠키에서도 토큰 확인 (대체 방법)
        if (requestToken == null && request.getCookies() != null) {
            requestToken = Arrays.stream(request.getCookies())
                    .filter(c -> "XSRF-TOKEN".equals(c.getName()))
                    .findFirst()
                    .map(c -> c.getValue())
                    .orElse(null);
        }

        if (sessionToken == null) {
            // 토큰이 없으면 생성
            sessionToken = generateCsrfToken(session, response);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF 토큰이 없습니다. 새 토큰이 생성되었습니다");
            return;
        }

        if (requestToken == null || !sessionToken.equals(requestToken)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "CSRF 토큰이 유효하지 않습니다");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String generateCsrfToken(HttpSession session, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        session.setAttribute("CSRF_TOKEN", token);
        response.setHeader("X-CSRF-TOKEN", token);

        Cookie cookie = new Cookie("XSRF-TOKEN", token);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
        return token;
    }
}