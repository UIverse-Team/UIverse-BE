package com.jishop.popular.scheduler;

import com.jishop.popular.service.PopularCalculationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
@Component
@RequiredArgsConstructor
public class PopularProductScoreScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final PopularCalculationService popularCalculationService;

    private static final String MAIN_KEY_PREFIX = "popular_keywords:";
    private static final String GAP_KEY_PREFIX = "popular_keywords_gap:";

    /**
     * 매 시간마다 Redis에 저장된 인기 검색어를 바탕으로 연관 상품 점수를 계산하는 스케줄러
     */
    @Scheduled(cron = "0 0 * * * *")
    public void calculatePopularProductScore() {
        // 이전 시간대 key 생성
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousHour  = now.minusHours(1);
        String previousHourKey = MAIN_KEY_PREFIX + previousHour.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));

        popularCalculationService.calculateAndCacheResult(previousHourKey);

        // 이전 시간대 gapKey를 생성하고, gapKey에 저장된 데이터가 있으면 다음 시간대 키와 병합
        // gapKey에 저장된 데이터를 다음 시간대 계산에 반영시킴
        String previousHourGapKey = GAP_KEY_PREFIX + previousHour.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        Long gapCount = redisTemplate.opsForZSet().size(previousHourGapKey);

        if (gapCount != null && gapCount > 0) {
            String currentKey = MAIN_KEY_PREFIX + now.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
            redisTemplate.opsForZSet().unionAndStore(currentKey, previousHourGapKey, currentKey);
            redisTemplate.delete(previousHourGapKey);
        }
        log.info("상품 계산 및 캐시 저장 완료 : {}", previousHourKey);
    }
}
