package com.jishop.product.service;

import com.jishop.common.response.PageResponse;
import com.jishop.product.dto.response.TodaySpecialListResponse;

public interface ProductDiscountService {

    PageResponse<TodaySpecialListResponse> getProductsByDailyDeal(final int page, final int size);

    void updateDailyDeals();
}
