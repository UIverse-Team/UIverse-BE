package com.jiseller.stock.dto;

import jakarta.validation.constraints.*;

public record SaleProductSpec(
        @NotNull(message = "옵션 ID는 필수입니다.")
        Long optionId,

        @Max(value = 9999, message = "재고 수량은 9999개 이하여야 합니다")
        @Min(value = 0, message = "재고 수량은 0개 이상이어야 합니다")
        int stockQuantity
) {
}
