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
            String optionValue = option.optionValue();
            String[] colorAndSize = optionValue.split("/");

            if (colorAndSize.length == 2) {
                String color = colorAndSize[0];
                String size = colorAndSize[1];

                if (!fashionClothesOptions.containsKey(color)) {
                    fashionClothesOptions.put(color, new ArrayList<>());
                }
                SizeOption sizeOption = new SizeOption(
                        option.saleProductId(),
                        size,
                        option.optionExtra()
                );
                fashionClothesOptions.get(color).add(sizeOption);
            }
        }

        List<ColorSizeOption> colorSizeOptions = fashionClothesOptions.entrySet().stream()
                .map(entry -> new ColorSizeOption(entry.getKey(), entry.getValue()))
                .toList();

        return new FashionClothesOptionResponse(colorSizeOptions);
    }
}
