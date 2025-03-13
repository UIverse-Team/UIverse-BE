package com.jishop.trend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.saleproduct.repository.SaleProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SaleProductRepository saleProductRepository;      // 상품DB 검증
    private final RedisTemplate<String, Object> redisTemplate;      // Redis에 검색어 저장(key-value)
    private final ObjectMapper objectMapper;                        // 로그 -> json 변환


    /**
     * 검색어 처리 메서드
     *
     * 사용자가 입력한 검색어에 대해
     * 1. 검색어 유효성 검사 - 비속어, 유효하지 않은 값
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
        if(!isValidKeyword(keyword)){
            return false;
        }
        
        if(!isRelatedToSaleProduct(keyword)){
            return false;
        }

        // 검색어 검증이 완료되면, 검색어를 Redis에 저장
        String hourKey = "popular_keywords:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        redisTemplate.opsForZSet().incrementScore(hourKey, keyword, 1.0);

        // 1시간 후 자동 삭제를 위해 TTL 설정- hourKey에 해당하는 전체 ZSet(key, keyword, score) 삭제
        redisTemplate.expire(hourKey, Duration.ofHours(1));

        // 로그 데이터 생성 및 전송 - 검색어, 사용자IP, 타임스탬프
        Map<String, Object> logData = new HashMap<>();
        logData.put("keyword", keyword);
        logData.put("clientIp", clientIp);
        logData.put("timestamp", System.currentTimeMillis());

        try{
            String logJson = objectMapper.writeValueAsString(logData);
            LoggerFactory.getLogger(SearchServiceImpl.class).info(logJson);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 검색어 유효성 검사 메서드
     * - null 체크, 빈 값 체크
     * - 추후 비속어/금칙어 필터링 로직이 필요
     *
     * @param keyword   입력받은 검색어
     * @return          유효하면 true, 아니면 false
     */
    @Override
    public boolean isValidKeyword(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()){
            return false;
        }

        return true;
    }

    /**
     * 검색어와 상품 데이터 관련성 확인 메서드
     *
     * @param keyword   입력받은 검색어
     * @return          관련 상품이 존재하면 true, 아니면 false
     */
    @Override
    public boolean isRelatedToSaleProduct(String keyword) {
        return saleProductRepository.existsByNameContaining(keyword);
    }
}
