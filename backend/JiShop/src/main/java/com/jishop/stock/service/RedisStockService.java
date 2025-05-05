package com.jishop.stock.service;

public interface RedisStockService{
    boolean checkStock(Long saleProductId, int quantity);
    boolean decreaseStock(Long saleProductId, int quantity);
    void syncStockDecrease(Long saleProductId, int quantity);
    void syncStockIncrease(Long saleProductId, int quantity);
    void syncCacheWithDb(Long saleProductId, int quantity);
}
