package com.jishop.logshop.service;

import com.jishop.common.response.PageResponse;
import com.jishop.logshop.dto.RecentProductResponse;

public interface RecentProductService {
     PageResponse<RecentProductResponse> recentProducts(Long id, int page);
}
