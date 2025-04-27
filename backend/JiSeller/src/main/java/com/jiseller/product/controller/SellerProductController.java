package com.jiseller.product.controller;

import com.jiseller.product.dto.CategoryInfoRequest;
import com.jiseller.product.dto.ImageUrlRequest;
import com.jiseller.product.dto.ProductRegistrationRequest;
import com.jiseller.product.dto.StatusRequest;
import com.jiseller.product.service.SellerProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/products")
public class SellerProductController {

    private final SellerProductService sellerProductService;

    @PostMapping("/create")
    public Long createProduct(@RequestBody @Valid final ProductRegistrationRequest productRegistrationRequest) {
        return sellerProductService.registerProduct(productRegistrationRequest);
    }

    // Todo:
    //  상품 update , delete
    //  옵션(업을 경우) 등록
    //  스토어(없을 경우) 등록
}
