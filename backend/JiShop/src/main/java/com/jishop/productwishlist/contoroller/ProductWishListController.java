package com.jishop.productwishlist.contoroller;

import com.jishop.member.domain.User;
import com.jishop.productwishlist.dto.ProductWishProductRequest;
import com.jishop.productwishlist.dto.ProductWishProductResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "찜 API")
public interface ProductWishListController {

    ResponseEntity<String> addWishProduct(User user, ProductWishProductRequest request);
    List<ProductWishProductResponse> getWishProducts(User user);
}
