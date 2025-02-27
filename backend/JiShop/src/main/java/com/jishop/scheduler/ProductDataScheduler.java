package com.jishop.scheduler;

import com.jishop.service.impl.ProductDataServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDataScheduler {

    private final ProductDataServiceImpl productDataServiceImpl;

    @Scheduled(cron = "0 0 1 * * ?")
    public void fetchProductData() {
        productDataServiceImpl.fetchAndSaveProductData();
    }
}
