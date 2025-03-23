package com.jishop.cart.dto;

import java.util.List;

public record DeleteCartRequest(
        List<Long> cartIdList
) {
}
