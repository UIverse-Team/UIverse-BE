package com.jishop.popular.dto;

import java.util.List;

public record PopularResponse(
        String rank,
        String keyword,
        // TODO: 카테고리는 추후 고려
        // String caterogy,
        List<PopularProductResponse> products
) {
}
