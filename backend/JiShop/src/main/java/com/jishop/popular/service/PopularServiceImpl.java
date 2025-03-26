package com.jishop.popular.service;


import com.jishop.popular.dto.PopularKeywordResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PopularServiceImpl implements PopularService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PopularCalculationService popularCalculationService;

    private static final String RESULT_KEY_PREFIX = "popular_result:";

    /**
     * 인기 검색어와 검색어별 인기 상품 리스트를 반환하는 메서드
     * @return 결과
     */
    public PopularKeywordResponse getPopularKeywordsAndProducts(){
        LocalDateTime now = LocalDateTime.now();
        String previousHourKey = now.minusHours(1).format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        String resultKey = RESULT_KEY_PREFIX + previousHourKey;

        Object cachedResult = redisTemplate.opsForValue().get(resultKey);

        if(cachedResult != null && cachedResult instanceof PopularKeywordResponse){
            return  (PopularKeywordResponse) cachedResult;
        }
        // 캐시된 결과가 없는 경우 상품 점수 계산을 통해 결과를 얻음
        else {
            return popularCalculationService.calculateAndCacheResult(previousHourKey);
        }
    }
}
