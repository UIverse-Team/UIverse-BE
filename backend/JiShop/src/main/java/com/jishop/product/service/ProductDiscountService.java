package com.jishop.product.service;

import com.jishop.product.dto.response.TodaySpecialListResponse;
import org.springframework.data.web.PagedModel;

public interface ProductDiscountService {

    PagedModel<TodaySpecialListResponse> getProductsByDailyDeal(final int page, final int size);

    void updateDailyDeals();
}
