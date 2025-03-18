package com.jishop.product.service;

import com.jishop.product.dto.ProductPageRequest;
import com.jishop.product.dto.ProductResponse;
import org.springframework.data.web.PagedModel;


public interface ProductService {

    PagedModel<ProductResponse> getProductList(ProductPageRequest productPageRequest);

    ProductResponse getProduct(Long id);
}
