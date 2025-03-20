package com.jishop.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartRequest(
        @NotNull
        Long cartId,
        @Positive
        int quantity
) {
}
