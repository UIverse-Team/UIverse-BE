package com.jishop.product.dto.response;

import com.jishop.product.domain.embed.CategoryInfo;

public record ProductCategoryInfoResponse(
        Long lCatId,
        Long mCatId,
        Long sCatId
) {
    public static ProductCategoryInfoResponse from(CategoryInfo categoryInfo) {
        return new ProductCategoryInfoResponse(
                categoryInfo.getLCatId(),
                categoryInfo.getMCatId(),
                categoryInfo.getSCatId()
        );
    }
}
