package com.jishop.option.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FashionClothesOptionResponse(Map<String, List<SizeOption>> colorAndSizes) {
    public static FashionClothesOptionResponse from(final List<Map<String, Object>> productOptions) {
        final Map<String, List<SizeOption>> fashionClothesOptions = new HashMap<>();

        for (final Map<String, Object> option : productOptions) {
            final String optionValue = (String) option.get("optionValue");
            final String[] colorAndSize = optionValue.split("/");

            if (colorAndSize.length == 2) {
                final String color = colorAndSize[0];
                final String size = colorAndSize[1];

                if (!fashionClothesOptions.containsKey(color)) {
                    fashionClothesOptions.put(color, new ArrayList<>());
                }
                final SizeOption sizeOption = new SizeOption(
                        (Long) option.get("saleProductId"),
                        size,
                        option.get("optionExtra")
                );
                fashionClothesOptions.get(color).add(sizeOption);
            }
        }

        return new FashionClothesOptionResponse(fashionClothesOptions);
    }
}
