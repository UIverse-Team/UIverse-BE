package com.jishop.category.controller;

import com.jishop.category.dto.HeaderProductListResponse;
import com.jishop.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    @Override
    @GetMapping
    public PagedModel<HeaderProductListResponse> getProductListByCategory(
            @RequestParam(defaultValue = "50000000L") Long categoryId,
            @RequestParam(defaultValue = "0") int page) {
        if (page < 0 || page > 100) {page = 0;}

        return categoryService.getProductsByCategory(categoryId, page);
    }
}
