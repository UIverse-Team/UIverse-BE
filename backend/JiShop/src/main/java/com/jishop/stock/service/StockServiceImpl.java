package com.jishop.stock.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.stock.domain.Stock;
import com.jishop.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final RedisStockService redisStockService;

    @Override
    @Transactional
    public void decreaseStock(Stock stock, int quantity) {
        stock.decreaseStock(quantity);

        redisStockService.syncCacheWithDb(stock.getSaleProduct().getId(), stock.getQuantity());
    }

    @Override
    @Transactional
    public void increaseStock(Stock stock, int quantity) {
        stock.increaseStock(quantity);

        redisStockService.syncCacheWithDb(stock.getSaleProduct().getId(), stock.getQuantity());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkStock(Stock stock, int quantity) {
        if(redisStockService.checkStock(stock.getSaleProduct().getId(), quantity))
            return true;

        return stock.hasStock(quantity);
    }
}