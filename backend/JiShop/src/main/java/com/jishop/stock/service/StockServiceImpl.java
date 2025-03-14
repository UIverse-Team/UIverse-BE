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

    // Update your interface to accept Stock object directly
    @Override
    @Transactional
    public void decreaseStock(Stock stock, int quantity) {
        stock.decreaseStock(quantity);
    }

    // Similar pattern for increaseStock
    @Override
    @Transactional
    public void increaseStock(Stock stock, int quantity) {
        stock.increaseStock(quantity);
    }

    // And for checkStock
    @Override
    @Transactional(readOnly = true)
    public boolean checkStock(Stock stock, int quantity) {
        return stock.hasStock(quantity);
    }

    private Stock findStockBySaleProductId(Long saleProductId) {
        return stockRepository.findBySaleProduct_Id(saleProductId)
                .orElseThrow(() -> new DomainException(ErrorType.STOCK_NOT_FOUND));
    }
}