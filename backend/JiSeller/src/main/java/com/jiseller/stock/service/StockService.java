package com.jiseller.stock.service;

import com.jiseller.stock.dto.SaleProductSpec;
import com.jiseller.saleproduct.domain.SaleProduct;

import java.util.List;

public interface StockService {

    void registerStocks(final List<SaleProduct> saleProducts, final List<SaleProductSpec> optionStockSpecs);
}
