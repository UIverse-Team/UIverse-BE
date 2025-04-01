package com.log.controller.impl;

import com.log.annotation.CurrentUser;
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
    public Long addPageLog(@RequestBody PageViewRequest request,
                           @CurrentUser Long userid) {
        return logService.addPageLog(request.addUserId(userid));
    }

    @Override
    @PatchMapping("/page/{id}/end")
    public void updatePage(@RequestBody PageViewRequest request,
                           @PathVariable Long id) {
        logService.updatePageLog(request, id);
    }

    @Override
    @PostMapping("/search")
    public void addSearch(@RequestBody SearchRequest request,
                          @CurrentUser Long userid) {
        logService.addSearchLog(request.addUserId(userid));
    }

    @Override
    @PostMapping("/product/click")
    public void addProductClick(@RequestBody ProductClickRequest requst,
                                @CurrentUser Long userid) {
        logService.addProductClick(requst.addUserId(userid));
    }

    @Override
    @PostMapping("/review")
    public void addSearch(@RequestBody ReviewRequest requst,
                          @CurrentUser Long userid) {
        logService.addReviewLog(requst.addUserId(userid));
    }
}
