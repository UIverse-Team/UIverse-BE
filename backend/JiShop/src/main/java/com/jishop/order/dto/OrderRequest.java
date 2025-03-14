package com.jishop.order.dto;

import com.jishop.order.domain.Order;
import jakarta.validation.constraints.NotBlank;

import java.util.*;

public record OrderRequest(
        @NotBlank(message = "수신자는 필수로 입력해야 합니다")
        String receiver,

        @NotBlank(message = "수신자 번호는 필수로 입력해야 합니다")
        String receiverNumber,

        @NotBlank(message = "기본주소 정보는 필수로 입력해야 합니다")
        String baseAddress,

        @NotBlank(message = "상세주소 정보는 필수로 입력해야 합니다")
        String detailAddress,

        @NotBlank(message = "우편번호는 필수로 입력해야 합니다")
        String zipCode,
        List<OrderDetailRequest> orderDetailRequestList
){
    public Order toEntity(){
        return Order.builder()
                .receiver(receiver)
                .receiverNumber(receiverNumber)
                .baseAddress(baseAddress)
                .detailAddress(detailAddress)
                .zipCode(zipCode)
                .build();
    }
}
