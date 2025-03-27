package com.jishop.popular.controller;

import com.jishop.popular.dto.PopularKeywordResponse;
import com.jishop.popular.service.PopularService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

/**
 * 인기 검색어 조회 API
 * Redis ZSet에서 Top10 검색어를 조회해 반환
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/popular")
public class PopularControllerImpl implements PopularController {

    private final PopularService popularService;

    @Override
    @GetMapping
    public PopularKeywordResponse getPopularKeywordAndProduct() {
        return popularService.getPopularKeywordAndProduct();
    }
}
