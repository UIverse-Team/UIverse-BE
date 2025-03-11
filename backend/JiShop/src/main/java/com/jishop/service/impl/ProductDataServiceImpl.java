//package com.jishop.service.impl;
//
//import com.jishop.domain.ProductData;
//import com.jishop.dto.ProductDataRequest;
//import com.jishop.repository.ProductRepository;
//import com.jishop.service.ProductDataService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ProductDataServiceImpl implements ProductDataService {
//
//    private final RestTemplate restTemplate;
//    private final ProductRepository productRepository;
//
//    @Value("${api.product.url}")
//    private String apiUrl;
//
//    @Override
//    @Transactional
//    public void fetchAndSaveProductData() {
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
//                .queryParam("_start", 0)
//                .queryParam("_limit", 100);
//        ProductDataRequest[] productDataRequests = restTemplate.getForObject(
//                builder.toUriString(),
//                ProductDataRequest[].class
//        );
//        if (productDataRequests != null) {
//            for (ProductDataRequest request : productDataRequests) {
//                productRepository.save(request.toEntity());
//            }
//        }
//    }
//}
