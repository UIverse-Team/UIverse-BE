package com.jishop.product.service;

import com.jishop.product.dto.ProductRequest;
import com.jishop.product.dto.ProductResponse;
import org.springframework.data.web.PagedModel;

public interface ProductService {

    /**
     * @param request 상품 조회/검색 요청 정보
     * @return 페이징된 상품 응답 목록
     */
    PagedModel<ProductResponse> getProductList(ProductRequest request);

    ProductResponse getProduct(Long id);
}
