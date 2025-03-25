package com.jishop.popular.dto;

import java.util.List;

public record PopularKeywordAndProduct(
        String rank,
        String keyword,
        // TODO: 카테고리는 추후 고려
        // String caterogy,
        List<PopularProduct> products
) {
}
