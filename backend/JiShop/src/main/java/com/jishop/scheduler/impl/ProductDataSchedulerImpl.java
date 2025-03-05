package com.jishop.scheduler.impl;

import com.jishop.batch.ProductDataJobRunner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
//public class ProductDataSchedulerImpl implements ProductDataScheduler {
public class ProductDataSchedulerImpl implements ApplicationRunner {

    private final ProductDataJobRunner jobRunner;


//    @Override
//    @Scheduled(cron = "0 0 1 * * ?") // 새벽 1시 스케쥴링
//    public void fetchProductData() {
//        productDataServiceImpl.fetchAndSaveProductData();
//    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("애플리케이션 시작 후 배치 실행");
        jobRunner.runJob();
    }
}
