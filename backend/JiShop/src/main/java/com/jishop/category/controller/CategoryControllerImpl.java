package com.jishop.category.controller;

import com.jishop.category.dto.CategoryResponse;
import com.jishop.category.service.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryServiceImpl categoryServiceImpl;

    @Override
    @GetMapping("/{categoryId}")
    public PagedModel<CategoryResponse> getProductListByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page) {
        if (page < 0 || page > 100) {page = 0;}

        return categoryServiceImpl.getProductsByCategory(categoryId, page);
    }
}
