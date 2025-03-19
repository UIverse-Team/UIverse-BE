package com.jishop.order.dto;

import com.jishop.address.dto.AddressRequest;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.*;

public record OrderRequest(
        @Valid AddressRequest address,
        @NotEmpty(message = "주문 상품 목록은 비어있을 수 없습니다.")
        List<OrderDetailRequest> orderDetailRequestList
){
    public Order toEntity(User user) {
        return Order.builder()
                .userId(user != null ? user.getId() : null)
                .recipient(this.address.recipient())
                .phone(this.address.phone())
                .address(this.address.address())
                .detailAddress(this.address.detailAddress())
                .zonecode(this.address.zonecode())
                .build();
    }
}
