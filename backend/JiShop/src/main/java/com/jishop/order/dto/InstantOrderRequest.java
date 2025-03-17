package com.jishop.order.dto;

import com.jishop.address.dto.AddressRequest;
import jakarta.validation.Valid;

public record InstantOrderRequest(
        Long saleProductId,
        int quantity,
        AddressRequest address
) {
}
