package com.jiseller.saleproduct.service;

import com.jiseller.product.domain.Product;
import com.jiseller.stock.dto.SaleProductSpec;

import java.util.List;

public interface SaleProductService {

    void registerSaleProduct(final Product product, final List<SaleProductSpec> specs);
}
