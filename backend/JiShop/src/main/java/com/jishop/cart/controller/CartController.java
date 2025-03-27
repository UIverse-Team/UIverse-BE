package com.jishop.cart.controller;

import com.jishop.cart.dto.*;
import com.jishop.member.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "장바구니 API")
public interface CartController {

    ResponseEntity<CartResponse> getCartItems(User user);
    ResponseEntity<CartDetailResponse> addCartItem(User user, AddCartRequest request);
    ResponseEntity<CartDetailResponse> updateCartItem(User user, UpdateCartRequest request);
    ResponseEntity<String> removeCartItem(User user, DeleteCartRequest deleteCartRequest);
    ResponseEntity<CartResponse> getGuestCartItems(@RequestParam String saleProductId);
}