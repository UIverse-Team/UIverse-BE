package com.jishop.logshop.controller;

import com.jishop.common.response.PageResponse;
import com.jishop.logshop.dto.RecentProductResponse;
import com.jishop.logshop.service.RecentProductService;
import com.jishop.member.annotation.CurrentUser;
import com.jishop.member.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recent")
public class RecentProductControllerImpl implements RecentProductController {

    private final RecentProductService recentProductService;

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<RecentProductResponse>> recentProducts(@CurrentUser User user,
                                                                              @RequestParam(defaultValue = "1") int page) {

        return ResponseEntity.ok(recentProductService.recentProducts(user.getId(), page));
    }
}
