package com.jishop.option.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record FashionClothes(
        Map<String, List<SizeOption>> colorAndSize
) {
    public static FashionClothes getColorAndSize(List<Map<String, Object>> productOptions) {
            Map<String, List<SizeOption>> colorSizeMap = new HashMap<>();

        for (Map<String, Object> option : productOptions) {
            String optionValue = (String) option.get("optionValue");
            String[] splitValue = optionValue.split("/");

            if (splitValue.length == 2) {
                String color = splitValue[0];
                String size = splitValue[1];

                if (!colorSizeMap.containsKey(color)) {
                    colorSizeMap.put(color, new ArrayList<>());
                }

                SizeOption sizeOption = new SizeOption(
                        (Long) option.get("saleProductId"),
                        size,
                        option.get("optionExtra")
                );

                colorSizeMap.get(color).add(sizeOption);
            }
        }

        return new FashionClothes(colorSizeMap);
    }
}
