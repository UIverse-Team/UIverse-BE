package com.jishop.wishlist.contoroller;

import com.jishop.member.domain.User;
import com.jishop.wishlist.dto.WishProductRequest;
import com.jishop.wishlist.dto.WishProductResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "찜 API")
public interface WishListController {

    ResponseEntity<String> addWishProduct(User user, WishProductRequest request);
    List<WishProductResponse> getWishProducts(User user);
}
