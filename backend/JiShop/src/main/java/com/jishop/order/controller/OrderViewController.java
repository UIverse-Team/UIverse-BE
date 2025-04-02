package com.jishop.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.ui.Model;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "결제 페이지 렌더링 API")
public interface OrderViewController {

    @Operation(
            summary = "결제 페이지 렌더링 API",
            description = "회원이 장바구니에서 주문 시 결제 페이지를 렌더링하는 API"
    )
    String createOrderAndRedirect(String orderNumber, int amount, Model model);
}
