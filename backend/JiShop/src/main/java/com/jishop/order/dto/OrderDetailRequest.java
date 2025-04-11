package com.jishop.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderDetailRequest(
        @NotNull(message = "상품 정보는 필수입니다.")
        Long saleProductId,
        @Positive(message = "수량은 1개 이상이어야 합니다.")
        int quantity,
        Long cartId
) {
    //장바구니 Id 없는 경우를 대비한 생성자
    public OrderDetailRequest(Long saleProductId, int quantity){
        this(saleProductId, quantity, null);
    }
}
