package com.jishop.product.controller;

import com.jishop.member.domain.User;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductDetailResponse;
import com.jishop.product.dto.response.ProductResponse;
import com.jishop.product.dto.response.TodaySpecialListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;

import java.util.List;

@Tag(name = "상품 API")
public interface ProductController {

    @Operation(summary = "상품 목록 조회")
    PagedModel<ProductResponse> getProductList(final ProductRequest productRequest, final int page, final int size);

    // user가 null일 수 있음(비회원)
    @Operation(summary = "상품 단건 조회")
    ProductDetailResponse getProduct(final User user, final Long productId);

    @Operation(summary = "인기순(찜순) 상품 조회")
    List<ProductResponse> getProductByWishTopTen(final int page, final int size);

    @Operation(summary = "오늘의 특가 상품 조회")
    PagedModel<TodaySpecialListResponse> getProductByTodaySpecial(final int page, final int size);
}
