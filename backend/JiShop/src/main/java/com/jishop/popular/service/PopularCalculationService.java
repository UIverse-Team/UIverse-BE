package com.jishop.popular.service;

import com.jishop.popular.dto.PopularKeywordResponse;
import com.jishop.popular.dto.PopularProductResponse;
import com.jishop.productscore.domain.ProductScore;

import java.util.List;

public interface PopularCalculationService {
    PopularKeywordResponse calculateAndCacheResult(String key);
    List<PopularProductResponse> findPopularProductsByKeyword(String keyword, int limit);
    PopularProductResponse convertToPopularProductResponse(ProductScore productScore);
}
