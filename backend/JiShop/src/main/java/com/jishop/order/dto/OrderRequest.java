package com.jishop.order.dto;

import com.jishop.address.dto.AddressRequest;
import com.jishop.member.domain.User;
import com.jishop.order.domain.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.*;

public record OrderRequest(
        @Valid AddressRequest address,
        List<OrderDetailRequest> orderDetailRequestList
){
    public Order toEntity(User user){
        return Order.builder()
                .userId(user.getId())
                .recipient(address.recipient())
                .phone(address.phone())
                .address(address.address())
                .detailAddress(address.detailAddress())
                .zonecode(address.zonecode())
                .build();
    }
}
