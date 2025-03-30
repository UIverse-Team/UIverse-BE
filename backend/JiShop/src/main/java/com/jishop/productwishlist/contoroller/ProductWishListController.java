package com.jishop.productwishlist.contoroller;

import com.jishop.member.domain.User;
import com.jishop.productwishlist.dto.ProductWishProductRequest;
import com.jishop.productwishlist.dto.ProductWishProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "찜 API")
public interface ProductWishListController {

    @Operation(summary = "상품 찜 추가/삭제")
    ResponseEntity<String> addWishProduct(User user, ProductWishProductRequest request);
    @Operation(summary = "상품 찜 목록")
    List<ProductWishProductResponse> getWishProducts(User user);
}
