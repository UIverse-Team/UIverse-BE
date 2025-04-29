package com.jiseller.saleproduct.service;

import com.jiseller.option.domain.Option;
import com.jiseller.product.domain.Product;

import java.util.List;

public interface SaleProductService {

    void registerSaleProduct(final Product product, final List<Option> options, final int stockQuantity);
}
