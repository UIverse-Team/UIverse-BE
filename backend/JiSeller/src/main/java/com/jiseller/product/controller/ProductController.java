package com.jiseller.product.controller;

import com.jiseller.product.dto.RegisterProductRequest;
import com.jiseller.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public void createProduct(@RequestBody @Valid final RegisterProductRequest registerProductRequest) {
        productService.registerProduct(registerProductRequest);
    }

    // Todo:
    //  상품 update , delete
    //  옵션(업을 경우) 등록
    //  스토어(없을 경우) 등록
}
