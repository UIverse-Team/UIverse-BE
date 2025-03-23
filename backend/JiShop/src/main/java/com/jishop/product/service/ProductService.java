package com.jishop.product.service;

import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductListResponse;
import com.jishop.product.dto.response.ProductResponse;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface ProductService {

    PagedModel<ProductListResponse> getProductList(ProductRequest productRequest, int page, int size);

    ProductResponse getProduct(Long userId,  Long productId);

    List<ProductListResponse> getProductByWishTopTen();
}
