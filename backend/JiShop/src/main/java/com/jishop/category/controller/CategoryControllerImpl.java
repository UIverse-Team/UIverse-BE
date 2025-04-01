package com.jishop.category.controller;

import com.jishop.category.dto.CategoryResponse;
import com.jishop.category.service.CategoryService;
import com.jishop.product.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
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
    @GetMapping("/products")
    public PagedModel<ProductResponse> getProductListByCategory(
            @RequestParam(defaultValue = "50000000L") Long categoryId,
            @RequestParam(defaultValue = "0") int page) {
        if (page < 0 || page > 100) {page = 0;}

        return categoryService.getProductsByCategory(categoryId, page);
    }

    // 진입 시점에 부르면 내려감 헤더용
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
