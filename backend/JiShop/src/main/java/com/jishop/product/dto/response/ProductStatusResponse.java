package com.jishop.product.dto.response;

import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Labels;
import com.jishop.product.domain.SaleStatus;
import com.jishop.product.domain.embed.Status;

public record ProductStatusResponse(
        Boolean secret,
        SaleStatus saleStatus,
        Labels labels,
        DiscountStatus discountStatus,
        Boolean isDiscount
) {
    public static ProductStatusResponse from(Status status) {
        return new ProductStatusResponse(
                status.getSecret(),
                status.getSaleStatus(),
                status.getLabels(),
                status.getDiscountStatus(),
                status.getIsDiscount()
        );
    }
}
