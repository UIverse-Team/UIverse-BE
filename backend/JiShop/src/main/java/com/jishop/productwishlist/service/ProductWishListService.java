package com.jishop.productwishlist.service;

import com.jishop.member.domain.User;
import com.jishop.productwishlist.dto.ProductWishProductRequest;
import com.jishop.productwishlist.dto.ProductWishProductResponse;

import java.util.List;

public interface ProductWishListService {

    void addProduct(User user, ProductWishProductRequest request);
    List<ProductWishProductResponse> getWishProducts(User user);
}
