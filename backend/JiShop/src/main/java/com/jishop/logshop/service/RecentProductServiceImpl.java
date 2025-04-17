package com.jishop.logshop.service;

import com.jishop.common.response.PageResponse;
import com.jishop.log.LogService;
import com.jishop.log.dto.ProductClickLogDto;
import com.jishop.logshop.dto.RecentProductResponse;
import com.jishop.product.domain.Product;
import com.jishop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecentProductServiceImpl implements RecentProductService {

    private final LogService logService;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RecentProductResponse> recentProducts(Long userId, int page) {
        int size = 3;

        int total = logService.getTotalLogCountByType(userId, "productClick");

        List<ProductClickLogDto> recentProductClickLogs = logService.findRecentProductClickLogs(userId, page, size);

        List<RecentProductResponse> recentProductResponse = makeRecetProductResponse(recentProductClickLogs);
        // 날짜에 맞는 id 와 product 일치하는거 찾아서 dto로 만들기
        return PageResponse.from(recentProductResponse, page, size, total);
    }

    private List<RecentProductResponse> makeRecetProductResponse(List<ProductClickLogDto> recentProductClickLogs) {
        var logMap = makeLogMap(recentProductClickLogs);
        var productMap = makeProductMap(recentProductClickLogs);

        return logMap.entrySet().stream()
                .map(entry -> {
                    LocalDate key = entry.getKey();
                    List<Product> list = entry.getValue().stream()
                            .map(productMap::get)
                            .toList();
                    return RecentProductResponse.from(key, list);
                }).toList();
    }

    private Map<LocalDate, List<Long>> makeLogMap(List<ProductClickLogDto> recentProductClickLogs) {
        Map<LocalDate, List<Long>> logMap = new TreeMap<>(Collections.reverseOrder());

        for (ProductClickLogDto dto : recentProductClickLogs) {
            logMap.computeIfAbsent(dto.clickTime(), k -> new ArrayList<>()).add(dto.productId());
        }

        return logMap;
    }

    private Map<Long, Product> makeProductMap(List<ProductClickLogDto> recentProductClickLogs) {
        Set<Long> productId = recentProductClickLogs.stream()
                .map(ProductClickLogDto::productId)
                .collect(Collectors.toSet());
        // map이랑 엮어서 dto 만들기
        List<Product> products = productRepository.findAllById(productId);

        return products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
    }
}
