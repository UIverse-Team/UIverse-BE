package com.jishop.product.controller;

import com.jishop.product.dto.ProductPageRequest;
import com.jishop.product.dto.ProductResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.web.PagedModel;

@Tag(name = "상품 API")
public interface ProductController {

    PagedModel<ProductResponse> getProductList(ProductPageRequest productPageRequest);

    ProductResponse getProduct(Long id);
}
