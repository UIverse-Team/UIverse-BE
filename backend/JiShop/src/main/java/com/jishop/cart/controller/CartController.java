package com.jishop.cart.controller;

import com.jishop.cart.dto.*;
import com.jishop.member.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "장바구니 API")
public interface CartController {

    @Operation(summary = "회원 장바구니 조회", description = "회원이 장바구니에 들어갈 때 사용되는 API")
    ResponseEntity<CartResponse> getCartItems(User user);

    @Operation(summary = "회원 장바구니 추가", description = "회원이 장바구니에 물건을 담을 때 사용되는 API")
    ResponseEntity<CartDetailResponse> addCartItem(User user, AddCartRequest request);

    @Operation(summary = "회원 장바구니 수량 변경", description = "회원이 장바구니 내에서 수량 변경을 할 때 사용되는 API")
    ResponseEntity<CartDetailResponse> updateCartItem(User user, UpdateCartRequest request);

    @Operation(summary = "회원 장바구니 삭제", description = "회원이 장바구니에서 물건을 삭제할 때 사용되는 API")
    ResponseEntity<String> removeCartItem(User user, DeleteCartRequest deleteCartRequest);

    @Operation(summary = "비회원 장바구니 조회", description = "비회원 장바구니 조회에 필요한 saleProduct Id 와 가지고 있던 수량을 인코딩해서 전달해준다")
    ResponseEntity<CartResponse> getGuestCartItems(
            @Parameter(example = "%5B%7B%22id%22%3A1%2C%22quantity%22%3A5%7D%2C%7B%22id%22%3A4%2C%22quantity%22%3A2%7D%5D") String saleProductId);
}