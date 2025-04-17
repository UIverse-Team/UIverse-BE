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
     * 55분까지 입력된 검색어를 현재 시간대의 인기 검색어로 처리하고,
     * 56분부터는 인기 검색어에 해당하는 인기 상품 리스트를 반환하기 위해 상품 점수를 계산하는 시간으로 스케줄러 처리
     */
    @Scheduled(cron = "0 56 * * * *")
    public void calculatePopularProductScore() {
        LocalDateTime now = LocalDateTime.now();
        String currentHourKey = MAIN_KEY_PREFIX + now.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));

        popularCalculationService.calculateAndCacheResult(currentHourKey);

        // 이전 시간대 gapKey를 생성하고,  저장된 데이터가 있으면 현재 시간대 키와 병합
        // gapKey에 저장된 데이터를 다음 시간대 계산에 반영시킴
        String previousHourGapKey = GAP_KEY_PREFIX + now.minusHours(1).format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        Long gapCount = redisTemplate.opsForZSet().size(previousHourGapKey);

        if (gapCount != null && gapCount > 0) {
            String currentKey = MAIN_KEY_PREFIX + now.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
            redisTemplate.opsForZSet().unionAndStore(currentKey, previousHourGapKey, currentKey);
            redisTemplate.delete(previousHourGapKey);
        }
        log.info("상품 계산 및 캐시 저장 완료 : {}", currentHourKey);
    }
}
