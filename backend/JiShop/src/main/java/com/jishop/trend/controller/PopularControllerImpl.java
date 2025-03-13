package com.jishop.trend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.core.RedisTemplate;
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
@RequestMapping("/popular")
@RequiredArgsConstructor
public class PopularControllerImpl {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    public ResponseEntity<?> getPopularKewords() {
        String hourkey = "popular_keywords:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        
        // 내림차순으로 상위 10개 키워드 조회
        Set<Object> top10Keywords = redisTemplate.opsForZSet().reverseRange(hourkey, 0, 9);

        return ResponseEntity.ok(top10Keywords);
    }
}
