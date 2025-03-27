package com.jishop.option.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record GeneralOptionResponse(
        List<ProductOption> options
) {
    public static GeneralOptionResponse from(final List<Map<String, Object>> productOptions) {
        final List<ProductOption> generalOptions = new ArrayList<>();

        for (final Map<String, Object> option : productOptions) {
            ProductOption productOption = new ProductOption(
                    (Long) option.get("saleProductId"),
                    (String) option.get("optionValue"),
                    (int) option.get("optionExtra")
            );
            generalOptions.add(productOption);
        }

        return new GeneralOptionResponse(generalOptions);
    }
}
