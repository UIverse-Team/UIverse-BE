package com.jishop.product.controller;

import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductDetailResponse;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.dto.response.TodaySpecialListResponse;
import com.jishop.product.service.ProductCategoryService;
import com.jishop.product.service.ProductDiscountService;
import com.jishop.product.service.ProductService;
import com.jishop.product.service.ProductWishlistService;
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
    private final ProductWishlistService productWishListService;
    private final ProductCategoryService productCategoryService;
    private final ProductDiscountService productDiscountService;

    @Override
    @GetMapping
    public PagedModel<ProductResponse> getProductList(
            @Validated final ProductRequest productRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        if (page < 0 || page > 100) {page = 0;}
        if (size <= 0 || size > 100) {size = 12;}

        return productService.getProductList(productRequest, page, size);
    }

    // user가 null일 수 있음(비회원)
    @Override
    @GetMapping("/{productId}")
    public ProductDetailResponse getProduct(@CurrentUser final User user,  @PathVariable final Long productId) {
        return productService.getProduct(user, productId);
    }

    @Override
    @GetMapping("/popular")
    public List<ProductResponse> getProductsByWishTopTen(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        if (page < 0 || page > 100) {page = 0;}
        if (size <= 0 || size > 100) {size = 8;}

        return productWishListService.getProductsByWishList(page, size);
    }

    @Override
    @GetMapping("/specialPrices")
    public PagedModel<TodaySpecialListResponse> getProductsByTodaySpecial(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        if (page < 0 || page > 100) {page = 0;}
        if (size <= 0 || size > 100) {size = 8;}

        return productDiscountService.getProductsByDailyDeal(page, size);
    }

    @Override
    @GetMapping("/category/{categoryId}")
    public PagedModel<ProductResponse> getProductsByCategory(
            @PathVariable final Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        if (page < 0 || page > 100) {page = 0;}
        if (size <= 0 || size > 100) {size = 12;}

        return productCategoryService.getProductsByCategory(categoryId, page, size);
    }
}
