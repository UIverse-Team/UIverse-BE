package com.jishop.productscore.service;

import com.jishop.productscore.domain.ProductScore;
import com.jishop.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductScoreService {
    void calculateAndUpdateScore(List<Product> products);
    ProductScore calculateScore(Product product);
    BigDecimal normalizeScore(int value, int maxValue);
}
