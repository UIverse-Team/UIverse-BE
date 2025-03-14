package com.jishop.product.controller;

import com.jishop.product.dto.GetProductResponse;
import com.jishop.product.repository.ProductRepository;
import com.jishop.product.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductControllerImpl implements ProductController {

    private final ProductRepository productRepository;
    private final ProductServiceImpl productServiceImpl;

    @GetMapping("/{id}")
    public GetProductResponse getProduct(@PathVariable Long id) {
        return productServiceImpl.getProductList(id);
    }
}
