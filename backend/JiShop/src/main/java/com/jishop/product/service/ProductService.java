package com.jishop.product.service;

import com.jishop.member.domain.User;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductDetailResponse;
import com.jishop.product.dto.response.ProductResponse;
import org.springframework.data.web.PagedModel;

public interface ProductService {

    PagedModel<ProductResponse> getProductList(final ProductRequest productRequest, final int page, final int size);

    ProductDetailResponse getProduct(final User user, final Long productId);
}
