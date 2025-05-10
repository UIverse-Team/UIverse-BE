package com.jishop.stock.service;

import java.util.Map;

public interface RedisStockService{
    boolean checkStock(Long saleProductId, int quantity);
    boolean checkMultipleStocks(Map<Long, Integer> productQuantityMap);
    boolean decreaseStock(Long saleProductId, int quantity);
    boolean decreaseMultipleStocks(Map<Long, Integer> productQuantityMap);
    void syncStockDecrease(Long saleProductId, int quantity);
    void syncStockIncrease(Long saleProductId, int quantity);
    void syncCacheWithDb(Long saleProductId, int quantity);
    Integer getStockFromCache(Long saleProductId);
}
