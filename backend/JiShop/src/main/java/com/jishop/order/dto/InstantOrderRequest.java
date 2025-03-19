package com.jishop.order.dto;

import com.jishop.address.dto.AddressRequest;
import jakarta.validation.constraints.Positive;

public record InstantOrderRequest(
        Long saleProductId,
        @Positive
        int quantity,
        AddressRequest address
) {
}
