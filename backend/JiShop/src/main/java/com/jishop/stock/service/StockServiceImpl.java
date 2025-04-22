package com.jishop.stock.service;

import com.jishop.stock.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final RedisStockService redisStockService;

    @Override
    @Transactional
    public void decreaseStock(Stock stock, int quantity) {
        redisStockService.syncStockDecrease(stock.getSaleProduct().getId(), quantity);
    }

    @Override
    @Transactional
    public void increaseStock(Stock stock, int quantity) {
        redisStockService.syncStockIncrease(stock.getSaleProduct().getId(), quantity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkStock(Stock stock, int quantity) {
        return redisStockService.checkStock(stock.getSaleProduct().getId(), quantity);
    }
}