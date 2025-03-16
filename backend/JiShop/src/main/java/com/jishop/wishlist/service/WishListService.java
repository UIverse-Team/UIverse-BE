package com.jishop.wishlist.service;

import com.jishop.member.domain.User;
import com.jishop.wishlist.dto.WishProductRequest;
import com.jishop.wishlist.dto.WishProductResponse;

import java.util.List;

public interface WishListService {

    void addProduct(User user, WishProductRequest request);
    List<WishProductResponse> getWishProducts(User user);
}
