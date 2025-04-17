package com.jishop.product.service;

import com.jishop.member.domain.User;
import com.jishop.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductWishlistService {

    List<ProductResponse> getProductsByWishList(final int page, final int size);

    boolean isProductWishedByUser(final User user, final Long productId);
}
