package com.jishop.product.controller;

import com.jishop.product.domain.Product;
import com.jishop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        productRepository.save(product);

        return ResponseEntity.ok("상품이 잘 생성되었습니다" + product);
    }
}
