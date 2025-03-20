package com.jishop.category.service;

import com.jishop.category.dto.CategoryResponse;
import com.jishop.category.repository.CategoryRepository;
import com.jishop.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public PagedModel<CategoryResponse> getProductsByCategory(Long categoryId, int page) {
        Pageable pageable = PageRequest.of(page, 12, Sort.by(Sort.Direction.DESC, "wishListCount"));
        Page<Product> productPage = categoryRepository.findProductsByTopLevelCategoryId(categoryId, pageable);

        Page<CategoryResponse> categoryResponsePage = productPage.map(product -> CategoryResponse.from(List.of(product)));

        return new PagedModel<>(categoryResponsePage);
    }
}
