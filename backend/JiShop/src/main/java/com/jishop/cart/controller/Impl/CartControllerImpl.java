package com.jishop.cart.controller.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.cart.controller.CartController;
import com.jishop.cart.dto.*;
import com.jishop.cart.service.CartService;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity<CartResponse> addCartItem(@CurrentUser User user, @RequestBody List<AddCartRequest> request) {
        CartResponse cartResponse = cartService.addCartItem(user, request);

        return ResponseEntity.ok(cartResponse);
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
    @GetMapping("/guest")
    public ResponseEntity<CartResponse> getGuestCartItems(@RequestParam String saleProductId) {
        try {
            // URL 디코딩
            String decodedJson = URLDecoder.decode(saleProductId, StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            List<GuestCartRequest> guestCartRequests = objectMapper.readValue(decodedJson, new TypeReference<>() {
            });

            CartResponse cartResponses = cartService.getGuestCart(guestCartRequests);
            return ResponseEntity.ok(cartResponses);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}