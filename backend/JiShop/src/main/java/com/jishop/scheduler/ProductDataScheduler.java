package com.jishop.scheduler;

import com.jishop.service.impl.ProductDataServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDataScheduler {

    private final ProductDataServiceImpl productDataServiceImpl;

//    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "*/10 * * * * *")
    public void fetchProductData() {
        System.out.println("Product data fetched at: " + LocalDateTime.now());
        productDataServiceImpl.fetchAndSaveProductData();
    }
}
