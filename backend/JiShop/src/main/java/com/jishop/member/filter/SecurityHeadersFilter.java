package com.jishop.member.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // 캐시 제어 - API 응답은 캐싱하지 않음
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        // 소셜 로그인 페이지인 경우 CSP를 완화하거나 적용하지 않음
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/oauth/login") || requestURI.contains("/oauth2/authorization") ||
                requestURI.contains("/login") || requestURI.endsWith(".html")) {
            // 소셜 로그인 관련 페이지에는 CSP 적용 안함
            filterChain.doFilter(request, response);
            return;
        }

        String csp = "default-src 'self' *; " +
                "script-src 'self' 'unsafe-inline' 'unsafe-eval' *; " +
                "style-src 'self' 'unsafe-inline' *; " +
                "img-src 'self' data: *; " +
                "connect-src 'self' *; " +
                "frame-src 'self' *; " +
                "font-src 'self' *; " +
                "object-src 'none'; " +
                "base-uri 'self'; " +
                "form-action 'self' *;";

        response.setHeader("Content-Security-Policy", csp);
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        filterChain.doFilter(request, response);
    }
}