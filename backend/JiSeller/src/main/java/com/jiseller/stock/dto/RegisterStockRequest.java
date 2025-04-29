package com.jiseller.stock.dto;

import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.stock.domain.Stock;

public record RegisterStockRequest(
        int quantity,
        SaleProduct saleProduct
) {
    public Stock toEntity() {
        return new Stock(quantity, saleProduct);
    }
}
