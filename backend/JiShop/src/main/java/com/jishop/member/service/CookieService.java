package com.jishop.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface CookieService {

    <T> void saveToCookie(String cookieName, T data, HttpServletResponse response, int maxAge);

    void deleteCookie(String cookieName, HttpServletResponse response) ;

    <T> Optional<T> getFromCookie(String cookieName, HttpServletRequest request, Class<T> type);
}
