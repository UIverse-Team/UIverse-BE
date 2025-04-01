package com.jishop.popular.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ProductRepository productRepository;              // 상품DB 검증
    private final RedisTemplate<String, Object> redisTemplate;      // Redis에 검색어 저장(key-value)
    private final ObjectMapper objectMapper;                        // 로그 -> json 변환

    private static final String MAIN_KEY_PREFIX = "popular_keywords:";
    private static final String GAP_KEY_PREFIX = "popular_keywords_gap:";

    /**
     * 검색어 처리 메서드
     *
     * 사용자가 입력한 검색어에 대해
     * 1. 검색어 유효성 검사 - 유효하지 않은 값(null, 빈 값)
     * 2. 검색어와 상품 데이터와 관련있는지 확인
     * 3. 유효한 검색어인 경우 Redis에 저장 - 현재 시간대별 누적 검색 횟수 저장
     * 4. 로그 데이터 생성 후 Logback을 통해 Logstash로 전송 - Elasticsearch에 저장
     *
     * @param keyword       사용자가 입력한 검색어
     * @param clientIp      클라이언트ip
     * @return              검색어가 유효하고 처리되었다면 true, 아니면 false
     */
    @Override
    public boolean processSearch(String keyword, String clientIp) {
        keyword = keyword.trim();

        if(!isValidKeyword(keyword)){
            log.info("유효하지 않은 검색어: {}", keyword);
            return false;
        }

        if(!isRelatedToSaleProduct(keyword)){
            log.info("상품 또는 쇼핑몰과 연관성이 없는 검색어: {}", keyword);
            return false;
        }

        // 검색어 검증이 완료되면, 검색어를 Redis에 저장
            // 00분 ~ 55분 까지는 메인 키에 저장 - 상품 계산에 사용
            // 55분 ~ 59분 까지는 여분 키에 저장 - 다음 시간대 상품 계산에 사용
            // 상품 계산 작업 처리 시간을 확보해 매 시간(정각)마다 계산된 데이터를 제공
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        String currentHourKey = now.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));

        // ♻️ 시연 및 테스트를 위해 Redis Key를 5분 단위로 생성
//        String minuteKey = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")).substring(0,11) + "0";

        // 1시간 후 자동 삭제를 위해 TTL 설정- hourKey에 해당하는 전체 ZSet(key, keyword, score) 삭제
            // TTL 설정 2시간으로 수정 - 다음 시간대에서 사용할 수 있도록 수정
        if(minute > 55){
            String gapKey = GAP_KEY_PREFIX + currentHourKey;
            redisTemplate.opsForZSet().incrementScore(gapKey, keyword, 1.0);
            redisTemplate.expire(currentHourKey, Duration.ofHours(2));
        }
        else {
            String mainKey = MAIN_KEY_PREFIX + currentHourKey;
            redisTemplate.opsForZSet().incrementScore(mainKey, keyword, 1.0);
            redisTemplate.expire(currentHourKey, Duration.ofHours(2));
        }

        // ♻️ 배포를 위해 Logstash(ELK) 제거
        // 로그 데이터 생성 및 전송 - 검색어, 사용자IP, 타임스탬프
//        Map<String, Object> logData = new HashMap<>();
//        logData.put("keyword", keyword);
//        logData.put("clientIp", clientIp);
//        logData.put("timestamp", System.currentTimeMillis());
//
//        // 로그 데이터(Map)를 JSON으로 구워서 Logstash로 전송
//        try{
//            String logJson = objectMapper.writeValueAsString(logData);
//            LoggerFactory.getLogger(SearchServiceImpl.class).info(logJson);
//        } catch (JsonProcessingException e){
//            e.printStackTrace();
//        }

        return true;
    }

    /**
     * 검색어 유효성 검사 메서드
     * - null 체크, 빈 값 체크
     *
     * @param keyword   입력받은 검색어
     * @return          유효하면 true, 아니면 false
     */
    @Override
    public boolean isValidKeyword(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()){
            return false;
        }
        log.info("검색어 유효성 검증 완료: {}", keyword);

        return true;
    }

    /**
     * 검색어와 상품 데이터 관련성 확인 메서드
     *
     * @param keyword   입력받은 검색어
     * @return          관련 상품 또는 쇼핑몰이 존재하면 true, 아니면 false
     */
    @Override
    public boolean isRelatedToSaleProduct(String keyword) {
        log.info("상품 또는 쇼핑몰과의 연관성 검증 완료: {}", keyword);

        return productRepository.existsByNameContaining(keyword) ||
                productRepository.existsByBrandContaining(keyword);
    }
}
