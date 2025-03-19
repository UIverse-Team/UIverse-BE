package com.jishop.product.controller;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.product.dto.ProductListResponse;
import com.jishop.product.dto.ProductRequest;
import com.jishop.product.dto.ProductResponse;
import com.jishop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
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
    @GetMapping
    public PagedModel<ProductListResponse> getProductList(@Validated ProductRequest request) {
        return productService.getProductList(request);
    }

    @Override
    @GetMapping("/{id}")
    public ProductResponse getProduct(@CurrentUser User user,  @PathVariable Long id) {
        return productService.getProduct(user, id);
    }
}
