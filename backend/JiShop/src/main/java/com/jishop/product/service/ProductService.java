package com.jishop.product.service;

import com.jishop.product.dto.ProductListRequest;
import com.jishop.product.dto.ProductResponse;
import com.jishop.product.dto.ProductSearchRequest;
import org.springframework.data.web.PagedModel;


public interface ProductService {

    PagedModel<ProductResponse> getProductList(ProductListRequest request);

    ProductResponse getProduct(Long id);

    PagedModel<ProductResponse> searchProducts(ProductSearchRequest request);
}
