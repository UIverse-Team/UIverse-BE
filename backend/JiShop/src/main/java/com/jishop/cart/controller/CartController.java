package com.jishop.cart.controller;

import com.jishop.cart.dto.AddCartRequest;
import com.jishop.cart.dto.CartDetailResponse;
import com.jishop.cart.dto.CartResponse;
import com.jishop.cart.dto.UpdateCartRequest;
import com.jishop.member.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "장바구니 API")
public interface CartController {
    ResponseEntity<CartResponse> getCartItems(User user);
    ResponseEntity<CartDetailResponse> addCartItem(User user, AddCartRequest request);
    ResponseEntity<CartDetailResponse> updateCartItem(User user, UpdateCartRequest request);
    ResponseEntity<String> removeCartItem(User user, Long cartId);
    ResponseEntity<CartResponse> getGuestCartItems(Long saleProductId);
}
