package com.jishop.category.controller;

import com.jishop.category.dto.CategoryResponse;
import com.jishop.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categoies")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    @Override
    @GetMapping("/{categoryId}")
    public PagedModel<CategoryResponse> getProductListByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page) {

        return categoryService.getProductsByCategory(categoryId, page);
    }
}
