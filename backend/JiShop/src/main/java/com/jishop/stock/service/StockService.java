package com.jishop.stock.service;

import com.jishop.stock.domain.Stock;

public interface StockService {

    void decreaseStock(Stock stock, int quantity);
    void increaseStock(Stock stock, int quantity);
    boolean checkStock(Stock stock, int quantity);
}
