package com.jishop.product.service;

import com.jishop.product.dto.ProductRequest;
import com.jishop.product.dto.ProductResponse;
import org.springframework.data.web.PagedModel;


public interface ProductService {

    PagedModel<ProductResponse> getProductList(ProductRequest productRequest);

    ProductResponse getProduct(Long id);

    PagedModel<ProductResponse> searchProducts(ProductRequest productRequest);
}
