package com.jishop.option.dto;

import java.util.ArrayList;
import java.util.List;

public record GeneralOptionResponse(
        List<ProductOption> options
) {
    public static GeneralOptionResponse from(final List<SizeOption> productOptions) {
        final List<ProductOption> generalOptions = new ArrayList<>();

        for (final SizeOption option : productOptions) {
            ProductOption productOption = new ProductOption(
                    option.saleProductId(),
                    option.optionValue(),
                    option.optionExtra()
            );
            generalOptions.add(productOption);
        }

        return new GeneralOptionResponse(generalOptions);
    }
}
