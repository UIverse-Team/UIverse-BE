package com.jishop.option.dto;

public record ProductOption(
        Long saleProductId,
        String optionValue,
        int optionExtra
) {
}
