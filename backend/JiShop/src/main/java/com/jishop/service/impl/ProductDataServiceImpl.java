package com.jishop.service.impl;

import com.jishop.dto.productdata.ProductDataRequest;
import com.jishop.repository.ProductRepository;
import com.jishop.service.ProductDataService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDataServiceImpl implements ProductDataService {

    private final RestTemplate restTemplate;
    private final ProductRepository productRepository;

    @Value("${api.product.url}")
    private String apiUrl;

    @Override
    @Transactional
    public void fetchAndSaveProductData() {
        long startTime = System.currentTimeMillis();
        int totalFetched = 0;
        int batchSize = 100;

        while (true) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("_start", totalFetched)
                    .queryParam("_limit", batchSize);
            ProductDataRequest[] productDataRequests = restTemplate.getForObject(
                    builder.toUriString(),
                    ProductDataRequest[].class
            );
            if (productDataRequests == null || productDataRequests.length == 0) {
                break; // 더 이상 데이터가 없으면 종료
            }
            for (ProductDataRequest request : productDataRequests) {
                productRepository.save(request.toEntity());
            }
            totalFetched += productDataRequests.length;
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        log.info("총 {}건의 데이터를 처리했습니다.", totalFetched, startTime, endTime, totalTime);
    }
}
