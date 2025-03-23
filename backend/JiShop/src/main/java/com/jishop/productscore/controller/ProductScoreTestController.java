package com.jishop.productscore.controller;

import com.jishop.product.domain.Product;
import com.jishop.product.repository.ProductRepository;
import com.jishop.productscore.service.ProductScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/productscore/test")
public class ProductScoreTestController {

    private final ProductScoreService productScoreService;
    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<String> calculateScores() {
        List<Product> products = productRepository.findAll();
        productScoreService.calculateAndUpdateScore(products);
        return ResponseEntity.ok("상품 점수 계산 완료");
    }
}
