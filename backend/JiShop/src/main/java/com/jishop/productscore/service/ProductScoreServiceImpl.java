package com.jishop.productscore.service;

import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.productscore.domain.ProductScore;
import com.jishop.productscore.repository.ProductScoreRepository;
import com.jishop.product.domain.Product;
import com.jishop.reviewproduct.repository.ReviewProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductScoreServiceImpl implements ProductScoreService {

    private final ProductScoreRepository productScoreRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ReviewProductRepository reviewProductRepository;

    // 가중치 점수 상수 정의
    private static final BigDecimal ORDER_COUNT = new BigDecimal("0.40");
    private static final BigDecimal REVIEW_RATING = new BigDecimal("0.25");
    private static final BigDecimal REVIEW_COUNT = new BigDecimal("0.20");
    private static final BigDecimal TOTAL_ORDER = new BigDecimal("0.15");

    /**
     * 리스트로 전달받은 모든 상품의 점수를 계산하고 저장
     * 상품 점수(ProductScore) 레포지토리에 이미 상품이 존재한다면, 업데이트
     * 상품 점수 계산에는 가중치를 적용
     * 
     * @param products      점수를 계산할 상풀 리스트
     */
    public List<ProductScore> calculateAndUpdateScore(List<Product> products) {
        List<ProductScore> productScores = new ArrayList<>();

        for(Product product : products) {
            Optional<ProductScore> productScore = productScoreRepository.findByProductId(product.getId());
            ProductScore newScore = calculateScore(product);

            if(productScore.isPresent()){
                ProductScore existingScore = productScore.get();
                existingScore.updateScores(newScore);
                productScores.add(existingScore);
            }
            else{
                productScores.add(newScore);
                productScoreRepository.save(newScore);
            }
        }

        return productScores;
    }

    /**
     * 상품 점수 계산 메서드
     *
     * @param product       점수를 계산할 상품
     * @return ProductScore 엔티티
     */
    public ProductScore calculateScore(Product product) {
        Long productId = product.getId();
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        
        // 최근 1달간 주문량, 리뷰 평점, 리뷰 수, 전체 주문량
        int recentOrderCount = safeInt(orderDetailRepository.countRecentOrdersByProductId(productId, oneMonthAgo));
        BigDecimal reviewRating = safeDecimal(reviewProductRepository.getReviewRatingByProductId(productId));
        int reviewCount = safeInt(reviewProductRepository.countReviewsByProductId(productId));
        int totalOrderCount = safeInt(orderDetailRepository.countTotalOrdersByProductId(productId));

        // 각 값들을 정규화(0.0 ~ 10.0 사이)
        BigDecimal recentOrderScore = normalizeScore(recentOrderCount, 50);
        BigDecimal reviewRatingScore = reviewRating.multiply(BigDecimal.valueOf(2));
        BigDecimal reviewCountScore = normalizeScore(reviewCount, 50);
        BigDecimal totalOrderScore = normalizeScore(totalOrderCount, 100);

        // 상품의 최종 점수 계산(가중치 적용)
        BigDecimal weightedScore = recentOrderScore.multiply(ORDER_COUNT)
                .add(reviewRatingScore.multiply(REVIEW_RATING))
                .add(reviewCountScore.multiply(REVIEW_COUNT))
                .add(totalOrderScore.multiply(TOTAL_ORDER));

        // ProductScore 엔티티 생성
        return ProductScore.builder()
                .product(product)
                .recentOrderCount(recentOrderCount)
                .reviewRating(reviewRating)
                .totalOrderCount(totalOrderCount)
                .recentOrderScore(recentOrderScore)
                .reviewRatingScore(reviewRatingScore)
                .reviewCountScore(reviewCountScore)
                .totalOrderScore(totalOrderScore)
                .weightedScore(weightedScore)
                .build();
    }

    /**
     * 단순히 count된 값들을 정규화
     * 
     * @param value     정규화할 값
     * @param maxValue  최대 값
     * @return 정규화된 점수(0.0 ~ 10.0사이)
     */
    public BigDecimal normalizeScore(int value, int maxValue) {
        return BigDecimal.valueOf(Math.min(value, maxValue) * 10.0 / maxValue)
                .setScale(1, RoundingMode.HALF_UP);
    }

    /**
     * repository에서 null을 반환할 경우 int로 처리
     * 
     * @param value     Integer값(null일 수 있음)
     * @return int값
     */
    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    /**
     * repository에서 null을 반환할 경우 BigDecimal로 처리
     * 
     * @param value     BigDecimal값(null일 수 있음)
     * @return BigDecimal값
     */
    private BigDecimal safeDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
