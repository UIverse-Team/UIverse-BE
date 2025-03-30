package com.jishop.option.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;

import java.util.*;

public record FashionClothesOptionResponse(
        @JsonValue Map<String, List<SizeOption>> option
) {
    public static FashionClothesOptionResponse from(List<Map<String, Object>> productOptions) {
        if (productOptions == null || productOptions.isEmpty()) {
            throw new DomainException(ErrorType.OPTION_NOT_FOUND);
        }

        Map<String, List<SizeOption>> fashionClothesOptions = new HashMap<>();

        for (Map<String, Object> option : productOptions) {
            String optionValue = (String) option.get("optionValue");
            String[] colorAndSize = optionValue.split("/");

            if (colorAndSize.length == 2) {
                String color = colorAndSize[0];
                String size = colorAndSize[1];

                if (!fashionClothesOptions.containsKey(color)) {
                    fashionClothesOptions.put(color, new ArrayList<>());
                }
                SizeOption sizeOption = new SizeOption(
                        (Long) option.get("saleProductId"),
                        size,
                        (int) option.get("optionExtra")
                );
                fashionClothesOptions.get(color).add(sizeOption);
            }
        }

        return new FashionClothesOptionResponse(fashionClothesOptions);
    }
}
