package com.jishop.member.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.member.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {

    private final ObjectMapper objectMapper;

    public <T> void saveToCookie(String cookieName, T data, HttpServletResponse response, int maxAge) {
        try {
            // 입력된 데이터를 바이트 배열로 직렬화 한걸 base64 인코딩한 문자열로 변환 -> 쿠키 안전 저장
            String encodedData = Base64.getEncoder().encodeToString(
                    objectMapper.writeValueAsBytes(data));

            ResponseCookie cookie = ResponseCookie.from(cookieName, encodedData)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .maxAge(Duration.ofSeconds(maxAge))
                    .path("/")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        } catch (Exception e) {
            throw new RuntimeException("쿠키 저장 중 오류가 발생했습니다", e);
        }
    }

    public void deleteCookie(String cookieName, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(0)
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public <T> Optional<T> getFromCookie(String cookieName, HttpServletRequest request, Class<T> type) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }
        // 요청된 쿠키들중 cookieName인 쿠키를 찾아오기
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst()
                .map(cookie -> {
                    try {
                        byte[] decodedBytes = Base64.getDecoder().decode(cookie.getValue());
                        return objectMapper.readValue(decodedBytes, type);
                    } catch (Exception e) {
                        throw new RuntimeException("쿠키 데이터 파싱 중 오류가 발생했습니다", e);
                    }
                });
    }
}
