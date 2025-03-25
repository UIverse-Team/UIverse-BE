package com.jishop.product.service;

import com.jishop.member.domain.User;
import com.jishop.product.dto.request.ProductRequest;
import com.jishop.product.dto.response.ProductListResponse;
import com.jishop.product.dto.response.ProductResponse;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface ProductService {

    PagedModel<ProductListResponse> getProductList(final ProductRequest productRequest, final int page, final int size);

    ProductResponse getProduct(final User user, final Long productId);

    List<ProductListResponse> getProductByWishTopTen();
}
