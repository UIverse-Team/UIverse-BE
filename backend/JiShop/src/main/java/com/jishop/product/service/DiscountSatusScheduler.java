package com.jishop.product.service;

import com.jishop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class DiscountSatusScheduler {

    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void randomPickDailyHotDeal() {
        log.info("오늘의 특가 랜덤 Pick Pick 시작합니다!!! 두구두구두구...");

        try {
            // 1. 기존 일일 특가 상품 초기화
            resetPreviousDailyDeals();

            // 2. 새로운 일일 특가 상품 설정
            updateNewDailyDeals();

            log.info("오늘의 특가 뽑기가 잘 완료되었습니다. 박수!!");
        } catch (Exception e) {
            log.error("오늘의 특가 뽑기 도중 에러가 발생했습니다.", e);
        }
    }

    private void resetPreviousDailyDeals() {
        String resetQuery = "UPDATE products SET discount_status = 'NONE', is_discount = false " +
                            "WHERE discount_status = 'DAILY_DEAL'";

        int updatedCount = jdbcTemplate.update(resetQuery);
        log.info(" {} 개의 상품이 리셋되었습니다. ", updatedCount);
    }

    private void updateNewDailyDeals() {
        String updateQuery = " UPDATE products p " +
                             " JOIN ( SELECT id " +
                                    " FROM products " +
                                    " WHERE discount_price < origin_price " +
                                    " AND sale_status = 'SELLING' " +
                                    " AND secret = false " +
                                    " ORDER BY RAND() " +
                                    " LIMIT 100 )" +
                             " AS selected_products ON p.id = selected_products.id " +
                             " SET p.discount_status = 'DAILY_DEAL', p.is_discount = true";

        int updatedCount = jdbcTemplate.update(updateQuery);
        log.info(" {}개의 상품이 오늘의 특가로 뽑혔습니다.", updatedCount);
    }
}
