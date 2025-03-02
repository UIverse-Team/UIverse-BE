package com.jishop.scheduler.impl;

import com.jishop.scheduler.ProductDataScheduler;
import com.jishop.service.impl.ProductDataServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
//public class ProductDataSchedulerImpl implements ProductDataScheduler {
public class ProductDataSchedulerImpl implements ApplicationRunner {


    private final ProductDataServiceImpl productDataServiceImpl;

//    @Override
//    @Scheduled(cron = "0 0 1 * * ?") // 새벽 1시 자동 실행
//    public void fetchProductData() {
//        productDataServiceImpl.fetchAndSaveProductData();
//    }

    // 상품데이터 가져오기 테스트용
    @Override
    public void run(ApplicationArguments args) throws Exception {
        productDataServiceImpl.fetchAndSaveProductData();
    }
}
