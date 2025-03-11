package com.jishop.product.service;

import com.jishop.product.domain.Stock;

public interface StockService {

    void decreaseStock(Long saleProductId, int quantity);
    void increaseStock(Long saleProductId, int quantity);
    boolean checkStock(Long saleProductId, int quantity);
}
