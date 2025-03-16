package com.jishop.wishlist.contoroller;

import com.jishop.member.domain.User;
import com.jishop.wishlist.dto.WishProductRequest;
import com.jishop.wishlist.dto.WishProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WishListController {

    ResponseEntity<String> addWishProduct(User user, WishProductRequest request);
    List<WishProductResponse> getWishProducts();
}
