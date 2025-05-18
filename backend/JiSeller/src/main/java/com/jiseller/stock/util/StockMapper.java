package com.jiseller.stock.util;

import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.stock.domain.Stock;

public class StockMapper {
    public static Stock toEntity(int quantity, SaleProduct saleProduct) {
        return new Stock(quantity, saleProduct);
    }
}