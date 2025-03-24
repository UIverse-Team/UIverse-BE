package com.jishop.cart.controller.Impl;

import com.jishop.cart.controller.CartController;
import com.jishop.cart.dto.*;
import com.jishop.cart.service.CartService;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartControllerImpl implements CartController {

    private final CartService cartService;

    //장바구니 조회
    @Override
    @GetMapping
    public ResponseEntity<CartResponse> getCartItems(@CurrentUser User user) {
        CartResponse cartResponse = cartService.getCart(user);

        return ResponseEntity.ok(cartResponse);
    }

    //장바구니 상품 추가
    @Override
    @PostMapping
    public ResponseEntity<CartDetailResponse> addCartItem(@CurrentUser User user, @RequestBody @Valid AddCartRequest request) {
        CartDetailResponse cartDetailResponse = cartService.addCartItem(user, request);

        return ResponseEntity.ok(cartDetailResponse);
    }

    //장바구니 상품 업데이트
    @Override
    @PutMapping
    public ResponseEntity<CartDetailResponse> updateCartItem(@CurrentUser User user, @RequestBody @Valid UpdateCartRequest request) {
        CartDetailResponse cartDetailResponse = cartService.updateCart(user, request);

        return ResponseEntity.ok(cartDetailResponse);
    }

    //장바구니 상품 삭제
    @Override
    @DeleteMapping
    public ResponseEntity<String> removeCartItem(@CurrentUser User user, @RequestBody DeleteCartRequest deleteCartRequest) {
        cartService.removeCartItem(user, deleteCartRequest);

        return ResponseEntity.ok("장바구니 상품이 잘 삭제되었습니다.");
    }

    //비회원 장바구니 조회
    @Override
    @GetMapping("/guest")
    public ResponseEntity<CartResponse> getGuestCartItems(@RequestParam List<Long> saleProductId) {
        CartResponse cartResponses = cartService.getGuestCart(saleProductId);

        return ResponseEntity.ok(cartResponses);
    }
}
