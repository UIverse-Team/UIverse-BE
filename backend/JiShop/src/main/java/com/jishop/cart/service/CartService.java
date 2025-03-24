package com.jishop.cart.service;

import com.jishop.cart.dto.*;
import com.jishop.member.domain.User;
import java.util.List;

public interface CartService {

    CartResponse getCart(User user);
    CartDetailResponse addCartItem(User user, AddCartRequest addCartRequest);
    CartDetailResponse updateCart(User user, UpdateCartRequest updateCartRequest);
    void removeCartItem(User user, DeleteCartRequest deleteCartRequest);
    CartResponse getGuestCart(List<Long> saleProductId);
}
