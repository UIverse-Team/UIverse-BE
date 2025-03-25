package com.log.controller.impl;

import com.log.controller.LogController;
import com.log.dto.PageViewRequest;
import com.log.dto.ProductClickRequest;
import com.log.dto.ReviewRequest;
import com.log.dto.SearchRequest;
import com.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/logs")
public class LogControllerImpl implements LogController {

    private final LogService logService;

    @Override
    @PostMapping("/page")
    public Long addPageLog(@RequestBody PageViewRequest request) {
        return logService.addPageLog(request);
    }

    @Override
    @PatchMapping("/page/{id}/end")
    public void updatePage(@RequestBody PageViewRequest request, @PathVariable Long id) {
        logService.updatePageLog(request, id);
    }

    @Override
    @PostMapping("/search")
    public void addSearch(@RequestBody SearchRequest request) {
        logService.addSearchLog(request);
    }

    @Override
    @PostMapping("/product/click")
    public void addProductClick(@RequestBody ProductClickRequest requst) {
        logService.addProductClick(requst);
    }

    @Override
    @PostMapping("/review")
    public void addSearch(@RequestBody ReviewRequest requst) {
        logService.addReviewLog(requst);
    }


}
