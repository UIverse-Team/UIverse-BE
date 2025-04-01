package com.jishop.logshop.controller;

import com.jishop.common.response.PageResponse;
import com.jishop.logshop.dto.RecentProductResponse;
import com.jishop.member.domain.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "최근 상품 API", description = "최근에 본 상품 API")
public interface RecentProductController {
    ResponseEntity<PageResponse<RecentProductResponse>> recentProducts(User user, int page);
}
