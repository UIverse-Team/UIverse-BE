package com.jishop.order.dto;

public record OrderDetailRequest(
        Long saleProductId,
        int quantity,
        Long cartId
) {
    //장바구니 Id 없는 경우를 대비한 생성자
    public OrderDetailRequest(Long saleProductId, int quantity){
        this(saleProductId, quantity, null);
    }
}
