package com.jishop.cart.service;

import com.jishop.cart.dto.AddCartRequest;
import com.jishop.cart.dto.CartDetailResponse;
import com.jishop.cart.dto.CartResponse;
import com.jishop.cart.dto.UpdateCartRequest;
import com.jishop.member.domain.User;
import org.springframework.stereotype.Service;

public interface CartService {

    CartResponse getCart(User user);
    CartDetailResponse addCartItem(User user, AddCartRequest addCartRequest);
    CartDetailResponse updateCart(User user, UpdateCartRequest updateCartRequest);
    void removeCartItem(User user, Long cartId);
}
