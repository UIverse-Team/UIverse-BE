package com.jishop.popular.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.popular.dto.PopularKeywordResponse;
import com.jishop.popular.dto.PopularProductResponse;
import com.jishop.popular.dto.PopularResponse;
import com.jishop.product.domain.Product;
import com.jishop.product.repository.ProductRepository;
import com.jishop.productscore.domain.ProductScore;
import com.jishop.productscore.service.ProductScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PopularCalculationServiceImpl implements PopularCalculationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    private final ProductScoreService productScoreService;
    private final ObjectMapper objectMapper;

    private static final String MAIN_KEY_PREFIX = "popular_keywords:";
    private static final String RESULT_KEY_PREFIX = "popular_result:";

    /**
     * Redis에 저장된 인기검색어를 기반으로 각 검색어별 인기 상품을 반환하고, 그 결과를 캐싱
     *
     * @param key   인기검색어가 저장된 key
     * @return 인기검색어와 인기상품 리스트
     */
    @Override
    public PopularKeywordResponse calculateAndCacheResult(String key) {
        //  Redis에서 시간대 key로 인기 검색어 상위 10개 가져오기
        String redisKey = MAIN_KEY_PREFIX + key;
        Set<ZSetOperations.TypedTuple<Object>> popularKewords = redisTemplate.opsForZSet()
                .reverseRangeWithScores(redisKey, 0, 9);

        List<PopularResponse> keywords = new ArrayList<>();
        int rank = 1;

        for(ZSetOperations.TypedTuple<Object> keyword : popularKewords) {
            String keywordValue = (String)keyword.getValue();
            keywordValue = keywordValue.replace("\"", "");

            List<PopularProductResponse> popularProducts = findPopularProductsByKeyword(keywordValue, 4);
            keywords.add(new PopularResponse(
                    String.valueOf(rank++),
                    keywordValue,
                    popularProducts));
        }

        PopularKeywordResponse response = new PopularKeywordResponse(
                redisKey.substring(redisKey.length() - 2),
                keywords
        );

        // 캐시에 결과 저장 및 TTL 설정
        String resultKey = RESULT_KEY_PREFIX + key;

        // JSON 역직렬화 오류로 String으로 Redis에 저장
        try{
            String stringResponse = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(resultKey, stringResponse);
            redisTemplate.expire(resultKey, Duration.ofHours(1));
        } catch(JsonProcessingException e) {
            log.error("인기 검색어 결과 JSON 직렬화 실패", e);
        }
        return response;
    }

    /**
     * 검색 키워드로 검색한 상품 목록 중 점수를 기준으로 상위 4(limit)개 인기상품 반환
     * 검색 키워드는 브랜드명을 우선순위로 두고 처리
     * 상품 점수가 0인 경우 상품 조회수를 기준으로 인기상품 반환
     *
     * @param keyword   검색 키워드
     * @param limit     반환할 상품 목록 개수
     * @return 상품 리스트
     */
    @Override
    public List<PopularProductResponse> findPopularProductsByKeyword(String keyword, int limit) {
        keyword = convertToBooleanString(keyword);
        List<Product> products;

        // 브랜드 이름과 정확히 일치하는 경우
        if(productRepository.existsByBrand(keyword)){
            products = productRepository.findAllByBrand(keyword);
        }
        // 그 외에는 Fulltext 검색으로 통합 처리
        else {
            products = productRepository.searchByNameOrBrandFulltext(keyword);
        }

        // 상품 리스트를 ProductScoreService로 전달해 상품들의 점수를 계산
        List<ProductScore> productScores = productScoreService.calculateAndUpdateScore(products);
    
        // 모든 상품 리스트의 점수가 0점인지 확인
        boolean allZeroScore =  productScores.stream()
                .allMatch(score -> score.getWeightedScore() == BigDecimal.ZERO);
        
        // 모든 상품 리스트의 점수가 0점이면 상품 조회수(product_view_count)를 기준으로 정렬
        if(allZeroScore){
            return products.stream()
                    .sorted(Comparator.comparing(Product::getProductViewCount).reversed())
                    .limit(limit)
                    .map(this::convertToPopularProductResponseFromProduct)
                    .toList();
        }
        
        else{
            return productScores.stream()
                    .sorted(Comparator.comparing(ProductScore::getWeightedScore).reversed())
                    .limit(limit)
                    .map(this::convertToPopularProductResponse)
                    .toList();
        }
    }

    /**
     * 상품 점수 정보를 기반으로 인기 상품 DTO로 변환
     *
     * @param productScore  상품 점수
     * @return 인기 상품 DTO
     */
    @Override
    public PopularProductResponse convertToPopularProductResponse(ProductScore productScore){
        Product product = productScore.getProduct();
        return new PopularProductResponse(
                product.getId(),
                product.getMainImage(),
                product.getBrand(),
                product.getName(),
                product.getOriginPrice(),
                product.getDiscountPrice(),
                productScore.getProduct().getDiscountRate(),
                productScore.getTotalOrderCount(),
                productScore.getReviewRating().doubleValue()
        );
    }

    /**
     * 상품 조회수를 기반으로 인기상품 DTO 반환
     * 
     * @param product   상품
     * @return 인기상품 DTO
     */
    public PopularProductResponse convertToPopularProductResponseFromProduct(Product product){
        return new PopularProductResponse(
                product.getId(),
                product.getMainImage(),
                product.getBrand(),
                product.getName(),
                product.getOriginPrice(),
                product.getDiscountPrice(),
                product.getDiscountRate(),
                0,
                0.0
        );
    }

    /**
     * 검색어 전처리 메서드
     * 검색어로 상품 리스트 조회 시 fulltext index를 boolean mode로 사용하기 위함
     *
     * @param keyword   입력받은 검색어
     * @return          전처리된 검색어
     */
    private String convertToBooleanString(String keyword){
        return Arrays.stream(keyword.trim().split("\\s+"))
                .filter(word -> !word.isBlank())
                .map(word -> "+" + word)
                .collect(Collectors.joining(" "));
    }
}
