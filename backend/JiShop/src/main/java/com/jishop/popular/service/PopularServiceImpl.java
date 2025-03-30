package com.jishop.popular.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.popular.dto.PopularKeywordResponse;
import com.jishop.popular.dto.PopularReponse;
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

    @Override
    public PopularKeywordResponse getTop5PopularKeywordsAndProducts() {
        return getPopularKeywordsAndProducts(5);
    }

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

        // ♻️ 시연 및 테스트를 위해 Redis Key를 5분 단위로 사용 및 20분 뒤 만료
//        String currentFiveMinutes = now
//                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
//                .substring(0, 11) + "0";
//        String resultKey = RESULT_KEY_PREFIX + currentFiveMinutes;

        String previousHourKey = now.minusHours(1).format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        String resultKey = RESULT_KEY_PREFIX + previousHourKey;

        String cachedResult = (String) redisTemplate.opsForValue().get(resultKey);

        if(cachedResult != null){
            try{
                PopularKeywordResponse fullResponse =  objectMapper.readValue(cachedResult, PopularKeywordResponse.class);
                return limitKeywords(fullResponse, limit);
            } catch (JsonProcessingException e) {
                log.error("캐시된 인기 검색어 결과 역직렬화 실패", e);
            }
        }

        PopularKeywordResponse calculatedResponse = popularCalculationService.calculateAndCacheResult(previousHourKey);
//        PopularKeywordResponse calculatedResponse = popularCalculationService.calculateAndCacheResult(currentFiveMinutes);
        return limitKeywords(calculatedResponse, limit);
    }

    private PopularKeywordResponse limitKeywords(PopularKeywordResponse response, int limit) {
        List<PopularReponse> limitedKeywords = response.keywords().stream()
                .limit(limit)
                .toList();
        return new PopularKeywordResponse(response.time(), limitedKeywords);
    }
}
