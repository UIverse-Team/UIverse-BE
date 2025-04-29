package com.jiseller.saleproduct.service;

import com.jiseller.product.domain.Product;

public interface SaleProductService {

    void registerSaleProduct(final Product product, final Long optionId);
}
