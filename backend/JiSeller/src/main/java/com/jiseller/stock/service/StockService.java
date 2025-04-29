package com.jiseller.stock.service;

import com.jiseller.saleproduct.domain.SaleProduct;

public interface StockService {

    void registerStock(final SaleProduct saleProduct, final int stockQuantity);
}
