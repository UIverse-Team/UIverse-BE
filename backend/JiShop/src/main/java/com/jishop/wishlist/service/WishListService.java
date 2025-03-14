package com.jishop.wishlist.service;

import com.jishop.wishlist.dto.WishProductRequest;
import com.jishop.wishlist.dto.WishProductResponse;

import java.util.List;

public interface WishListService {

    void addProduct(WishProductRequest request);
    List<WishProductResponse> getWishProducts();
}
