package com.jishop.crawler.config;

import com.jishop.crawler.NaverStoreCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 네이버 상품 데이터 크롤링 Runner 설정 클래스
 * application.yml의 crawler.enabled 속성을 통해 활성화/비활성화 가능
 */
@Configuration
public class NaverCrawlerConfig {

    private static final Logger log = LoggerFactory.getLogger(NaverCrawlerConfig.class);

    @Autowired
    private NaverStoreCrawler naverStoreCrawler;

    /**
     * 애플리케이션 시작 시 크롤링 작업을 실행하는 CommandLineRunner 빈
     * application.yml에서 crawler.enabled=true로 설정된 경우에만 활성화됨
     */
    @Bean
    @ConditionalOnProperty(name = "crawler.enabled", havingValue = "true", matchIfMissing = false)
    public CommandLineRunner naverProductCrawlerRunner() {
        return args -> {
            log.info("네이버 상품 크롤링 작업을 시작합니다...");

            try {
                // skipCrawling을 false로 설정하여 새로운 데이터 크롤링 실행
                naverStoreCrawler.crawlAndSaveProducts(false);
                log.info("네이버 상품 크롤링 작업이 성공적으로 완료되었습니다.");
            } catch (Exception e) {
                log.error("네이버 상품 크롤링 중 오류가 발생했습니다: {}", e.getMessage(), e);
            }
        };
    }

    /**
     * 기존 저장된 JSON 파일에서 데이터를 로드하여 DB에 저장하는 CommandLineRunner 빈
     * application.yml에서 crawler.load-only=true로 설정된 경우에만 활성화됨
     */
    @Bean
    @ConditionalOnProperty(name = "crawler.load-only", havingValue = "true", matchIfMissing = false)
    public CommandLineRunner naverProductLoaderRunner() {
        return args -> {
            log.info("네이버 상품 데이터 로드 작업을 시작합니다...");

            try {
                // skipCrawling을 true로 설정하여 크롤링 단계를 건너뛰고 저장된 JSON에서 데이터 로드
                naverStoreCrawler.crawlAndSaveProducts(true);
                log.info("네이버 상품 데이터 로드 작업이 성공적으로 완료되었습니다.");
            } catch (Exception e) {
                log.error("네이버 상품 데이터 로드 중 오류가 발생했습니다: {}", e.getMessage(), e);
            }
        };
    }
}