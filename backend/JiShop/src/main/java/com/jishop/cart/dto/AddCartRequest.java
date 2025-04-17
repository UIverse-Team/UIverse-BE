package com.jishop.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddCartRequest(
        @NotNull
        Long saleProductId,
        @Positive
        int quantity,
        boolean isForced
) {
}
