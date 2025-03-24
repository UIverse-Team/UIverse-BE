package com.jishop.cart.dto;

import java.util.List;
import java.util.Map;

public record GuestCartRequest(
    List<Map<Long, Integer>> saleProductId
) { }
