package com.jishop.batch;

import com.jishop.dto.productdata.ProductDataRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class ProductDataReader implements ItemReader<ProductDataRequest> {

    private final RestTemplate restTemplate;
    @Value("${api.product.url}")
    private String apiUrl;
    @Value("${api.product.data.start}")
    private int start;
    @Value("${api.product.data.limit}")
    private int limit;
    private Iterator<ProductDataRequest> productIterator;

    @Override
    public ProductDataRequest read() {
        if (productIterator == null || !productIterator.hasNext()) {
            loadNextBatch();
        }

        return (productIterator != null && productIterator.hasNext()) ? productIterator.next() : null;
    }

    private void loadNextBatch() {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("_start", start)
                    .queryParam("_limit", limit);

            ProductDataRequest[] productArray = restTemplate.getForObject(
                    builder.toUriString(),
                    ProductDataRequest[].class
            );
            if (productArray == null || productArray.length == 0) {
                productIterator = null;
                log.info("더 이상 가져올 제품이 없습니다. 배치 종료.");
            } else {
                List<ProductDataRequest> products = Arrays.asList(productArray);
                productIterator = products.iterator();
                start += products.size();
                log.info("{}개의 제품 데이터를 가져왔습니다.", products.size());
            }
        } catch (Exception e) {
            log.error("API에서 제품 데이터를 가져오는 중 오류 발생", e);
            productIterator = null;
        }
    }
}
