package com.jishop.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class DiscountStatusScheduler {

    private final ProductDiscountService productDiscountService;

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void randomPickDailyHotDeal() {
        log.info("오늘의 특가 랜덤 Pick Pick 시작합니다!!! 두구두구두구...");

        try {
            productDiscountService.updateDailyDeals();
            log.info("오늘의 특가 뽑기가 잘 완료되었습니다. 박수!!");
        } catch (Exception e) {
            log.error("오늘의 특가 뽑기 도중 에러가 발생했습니다.", e);
        }
    }
}
