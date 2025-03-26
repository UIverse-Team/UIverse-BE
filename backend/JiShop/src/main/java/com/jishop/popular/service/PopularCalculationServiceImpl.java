package com.jishop.popular.service;

import com.jishop.popular.dto.PopularKeywordResponse;
import com.jishop.popular.dto.PopularProductResponse;
import com.jishop.popular.dto.PopularReponse;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PopularCalculationServiceImpl implements PopularCalculationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;
    private final ProductScoreService productScoreService;

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
        //  Redis에서 이전 시간대 인기 검색어 상위 10개 가져오기
        String redisKey = MAIN_KEY_PREFIX + key;
        Set<ZSetOperations.TypedTuple<Object>> popularKewords = redisTemplate.opsForZSet()
                .reverseRangeWithScores(redisKey, 0, 9);

        List<PopularReponse> keywords = new ArrayList<>();
        int rank = 1;

        for(ZSetOperations.TypedTuple<Object> keyword : popularKewords) {
            String keywordValue = (String)keyword.getValue();
            keywordValue = keywordValue.replace("\"", "");

            List<PopularProductResponse> popularProducts = findPopularProductsByKeyword(keywordValue, 4);
            keywords.add(new PopularReponse(
                    String.valueOf(rank++),
                    keywordValue,
                    popularProducts));
        }
        PopularKeywordResponse response = new PopularKeywordResponse(redisKey, keywords);

        // 캐시에 결과 저장 및 TTL 설정
        String resultKey = RESULT_KEY_PREFIX + key;
        redisTemplate.opsForValue().set(resultKey, response);
        redisTemplate.expire(resultKey, Duration.ofHours(1));

        return response;
    }

    /**
     * 검색 키워드로 검색한 상품 목록 중 점수를 기준으로 상위 4(limit)개 인기상품 반환
     * 검색 키워드는 브랜드명을 우선순위로 두고 처리
     *
     * @param keyword   검색 키워드
     * @param limit     반환할 상품 목록 개수
     * @return 상품 리스트
     */
    @Override
    public List<PopularProductResponse> findPopularProductsByKeyword(String keyword, int limit) {
        List<Product> products;

        // 브랜드 이름과 정확히 일치하는 경우
        if(productRepository.existsByBrand(keyword)){
            products = productRepository.findAllByBrand(keyword);
        }
        // 브랜드 이름이 부분 포함된 경우
        else if(productRepository.existsByNameContaining(keyword)){
            products = productRepository.findAllByBrandContaining(keyword);
        }
        else{
            products = productRepository.findAllByNameContaining(keyword);
        }

        // 상품 리스트를 ProductScoreService로 전달해 상품들의 점수를 계산
        List<ProductScore> productScores = productScoreService.calculateAndUpdateScore(products);

        // 상품 점수를 기준으로 정렬하고 상위 4개 PopularProductResponse 반환
        return productScores.stream()
                .sorted(Comparator.comparing(ProductScore::getWeightedScore).reversed())
                .limit(limit)
                .map(this::convertToPopularProductResponse)
                .toList();
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
                productScore.getProduct().getDiscountRate().intValue(),
                productScore.getTotalOrderCount(),
                productScore.getReviewRating().doubleValue()
                // FE 요청으로 리뷰 평점 조정
                // 4.0 ~ 4.4까지는 4.0으로 표현, 4.5 ~ 4.9까지는 4.5로 표현
//                productScore.getReviewRating()
//                        .multiply(BigDecimal.valueOf(2))
//                        .setScale(0, RoundingMode.FLOOR)
//                        .divide(BigDecimal.valueOf(2))
//                        .doubleValue()

        );
    }
}
