package com.jishop.product.service;

public interface StockService {

    void decreaseStock(Long saleProductId, int quantity);
    void increaseStock(Long saleProductId, int quantity);
    boolean checkStock(Long saleProductId, int quantity);
}
