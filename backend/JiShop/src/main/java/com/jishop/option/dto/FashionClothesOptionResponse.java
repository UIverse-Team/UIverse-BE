package com.jishop.option.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FashionClothesOptionResponse(
        List<ColorSizeOption> option
) {
    public static FashionClothesOptionResponse from(List<SizeOption> productOptions) {
        if (productOptions == null || productOptions.isEmpty()) {
            return new FashionClothesOptionResponse(List.of());
        }

        Map<String, List<SizeOption>> fashionClothesOptions = new HashMap<>();

        for (SizeOption option : productOptions) {
            if (option.isValidOption()) {
                String color = option.extractColor();

                if (!fashionClothesOptions.containsKey(color)) {
                    fashionClothesOptions.put(color, new ArrayList<>());
                }

                SizeOption sizeOption = option.withSize();
                fashionClothesOptions.get(color).add(sizeOption);
            }
        }

        List<ColorSizeOption> colorSizeOptions = fashionClothesOptions.entrySet().stream()
                .map(entry -> new ColorSizeOption(entry.getKey(), entry.getValue()))
                .toList();

        return new FashionClothesOptionResponse(colorSizeOptions);
    }
}
