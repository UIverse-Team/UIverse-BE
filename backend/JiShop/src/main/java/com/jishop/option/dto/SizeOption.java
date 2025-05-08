package com.jishop.option.dto;

public record SizeOption(
        Long saleProductId,
        String optionValue,
        int optionExtra
) {
    public boolean isValidOption() {
        return (optionValue != null) && optionValue.split("/").length == 2;
    }

    public String extractColor() {
        String[] colorAndSize = optionValue.split("/");
        return colorAndSize[0];
    }

    public String extractSize() {
        String[] colorAndSize = optionValue.split("/");
        return colorAndSize[1];
    }

    public SizeOption withSize() {
        return new SizeOption(saleProductId, extractSize(), optionExtra);
    }
}
