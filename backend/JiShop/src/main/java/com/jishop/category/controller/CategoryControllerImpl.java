package com.jishop.category.controller;

import com.jishop.category.dto.CategoryResponse;
import com.jishop.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;

    @Override
    @GetMapping("/root")
    public List<CategoryResponse> getCategoryFilterInfo() {
        return categoryService.getCategoryFilterInfo();
    }

    @Override
    @GetMapping("/subcategories")
    public List<CategoryResponse> getSubcategoriesByParentId(
            @RequestParam(defaultValue = "0") Long categoryId) {
        return categoryService.getSubcategoriesByParentId(categoryId);
    }
}
