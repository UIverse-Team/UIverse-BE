package com.jishop.order.dto;

import com.jishop.address.dto.AddressRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "주소 정보는 필수입니다.")
        @Valid AddressRequest address,
        @NotEmpty(message = "주문 상품 목록은 비어있을 수 없습니다.")
        List<OrderDetailRequest> orderDetailRequestList
){ }
