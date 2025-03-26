package com.jishop.popular.service;


import com.jishop.popular.dto.PopularKeywordResponse;
import com.jishop.popular.dto.PopularReponse;
import com.jishop.popular.dto.PopularProductResponse;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PopularServiceImpl implements PopularService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ProductRepository productRepository;
    private final ProductScoreService productScoreService;

    /**
     * 현재 시간 기준 상위 10개 인기검색어와 각 검색어별 인기 상품을 조회해 반환
     * 
     * 1. Redis에서 인기 검색어 가져오기
     * 2. 각 검색어 별로 상품 리스트를 가져오기
     * 3. 상품 점수를 조회해 상위 4개의 상품을 추출
     * 4. 인기 검색어와 인기 상품을 반환
     *
     * @return 인기 검색어와 관련 상품 리스트
     */
    public PopularKeywordResponse getPopularKeywordAndProduct() {
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("HH"));

        // 1. Redis에서 인기 검색어 상위 10개 가져오기
        String hourkey = "popular_keywords:" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        Set<ZSetOperations.TypedTuple<String>> popularKewords = redisTemplate.opsForZSet()
                .reverseRangeWithScores(hourkey, 0, 9);
        
        List<PopularReponse> keywords = new ArrayList<>();
        int rank = 1;

        // 2. 각 검색어 별로 상품 정보를 가져와 상품 점수를 계산하고 상위 4개 인기 상품 리스트 반환
        for(ZSetOperations.TypedTuple<String> keyword : popularKewords){
            String keywordValue = keyword.getValue();
            keywordValue = keywordValue.replace("\"", "");

            List<PopularProductResponse> popularProducts = findPopularProductsByKeyword(keywordValue, 4);
            keywords.add(new PopularReponse(
                    String.valueOf(rank++),
                    keywordValue,
                    popularProducts));
        }

        return new PopularKeywordResponse(time, keywords);
    }

    /**
     * 검색 키워드로 검색한 상품 목록 중 점수를 기준으로 상위 4(limit)개 인기상품 반환
     *
     * @param keyword   검색 키워드
     * @param limit     반환할 상품 목록 개수
     * @return 상품 리스트
     */
    public List<PopularProductResponse> findPopularProductsByKeyword(String keyword, int limit) {

        // 키워드로 상품 검색
        List<Product> products = productRepository.findByNameContaining(keyword);

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
                productScore.getReviewRating().intValue()
        );
    }
}
