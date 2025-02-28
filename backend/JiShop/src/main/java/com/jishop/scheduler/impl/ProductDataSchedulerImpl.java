package com.jishop.scheduler.impl;

import com.jishop.scheduler.ProductDataScheduler;
import com.jishop.service.impl.ProductDataServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDataSchedulerImpl implements ProductDataScheduler {

    private final ProductDataServiceImpl productDataServiceImpl;

    @Override
//    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "*/30 * * * * *")
    public void fetchProductData() {
        productDataServiceImpl.fetchAndSaveProductData();
    }
}
