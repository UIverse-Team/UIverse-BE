package com.jishop.product.controller;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductListResponse;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @Override
    @GetMapping
    public PagedModel<ProductListResponse> getProductList(
            @Validated ProductRequest productRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        if (page < 0 || page > 100) {page = 0;}
        if (size <= 0 || size > 100) {size = 12;}

        return productService.getProductList(productRequest, page, size);
    }

    // user가 null일 수 있음(비회원)    @Override
    @GetMapping("/{productId}")
    public ProductResponse getProduct(@CurrentUser User user,  @PathVariable Long productId) {
        return productService.getProduct(user, productId);
    }

    @Override
    @GetMapping("/popular")
    public List<ProductListResponse> getProductByWishTopTen() {
        return productService.getProductByWishTopTen();
    }
}
