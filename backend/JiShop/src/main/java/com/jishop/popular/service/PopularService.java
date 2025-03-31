package com.jishop.popular.service;

import com.jishop.popular.dto.PopularKeywordResponse;
import com.jishop.popular.dto.PopularProductResponse;
import com.jishop.productscore.domain.ProductScore;

import java.util.List;

public interface PopularService {

    PopularKeywordResponse getTop5PopularKeywordsAndProducts();
    PopularKeywordResponse getTop10PopularKeywordsAndProducts();
    PopularKeywordResponse getPopularKeywordsAndProducts(int limit);
}