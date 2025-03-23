package com.jishop.product.controller;

import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductListResponse;
import com.jishop.product.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;

import java.util.List;

@Tag(name = "상품 API")
public interface ProductController {

    PagedModel<ProductListResponse> getProductList(ProductRequest productRequest, int page, int size);

    ProductResponse getProduct(Long userId, Long productId);

    List<ProductListResponse> getProductByWishTopTen();
}
