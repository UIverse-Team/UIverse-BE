package com.jishop.popular.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.popular.dto.PopularKeywordResponse;
import com.jishop.popular.dto.PopularResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PopularServiceImpl implements PopularService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PopularCalculationService popularCalculationService;

    private static final String RESULT_KEY_PREFIX = "popular_result:";

    /**
     * 쇼핑몰 메인 페이지에서 5위까지의 인기검색어와 인기상품리스트 반환
     * @return 결과
     */
    @Override
    public PopularKeywordResponse getTop5PopularKeywordsAndProducts() {
        return getPopularKeywordsAndProducts(5);
    }

    /**
     * 쇼핑몰 랭킹 디테일 페이지에서 10까지의 인기검색어와 인기상품리스트 반환
     * @return 결과
     */
    @Override
    public PopularKeywordResponse getTop10PopularKeywordsAndProducts() {
        return getPopularKeywordsAndProducts(10);
    }

    /**
     * 인기 검색어와 검색어별 인기 상품 리스트를 반환하는 메서드
     * @return 결과
     */
    @Override
    public PopularKeywordResponse getPopularKeywordsAndProducts(int limit){
        LocalDateTime now = LocalDateTime.now();

        String previousHourKey = now.minusHours(1).format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        String resultKey = RESULT_KEY_PREFIX + previousHourKey;

        String cachedResult = (String) redisTemplate.opsForValue().get(resultKey);

        // 캐시 히트 시 수행
        if(cachedResult != null){
            try{
                PopularKeywordResponse fullResponse =  objectMapper.readValue(cachedResult, PopularKeywordResponse.class);
                return limitKeywords(fullResponse, limit);
            } catch (JsonProcessingException e) {
                log.error("캐시된 인기 검색어 결과 역직렬화 실패", e);
            }
        }
        // 캐시 미스 시 수행
            // 캐스 스톰 문제 발생 가능성
            // 락 메커니즘 구현이 필요
        PopularKeywordResponse calculatedResponse = popularCalculationService.calculateAndCacheResult(previousHourKey);
        return limitKeywords(calculatedResponse, limit);
    }

    private PopularKeywordResponse limitKeywords(PopularKeywordResponse response, int limit) {
        List<PopularResponse> limitedKeywords = response.keywords().stream()
                .limit(limit)
                .toList();
        return new PopularKeywordResponse(response.time(), limitedKeywords);
    }
}
