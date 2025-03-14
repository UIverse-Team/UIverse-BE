package com.jishop.product.controller;

import com.jishop.product.dto.GetProductResponse;
import com.jishop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @Override
    @GetMapping("/{id}")
    public GetProductResponse getProduct(@PathVariable Long id) {
        return productService.getProductList(id);
    }
}
