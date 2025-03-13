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

    //재고 감소 처리
    @Override
    @Transactional
    public void decreaseStock(Long saleProductId, int quantity){
        Stock stock = findStockBySaleProductId(saleProductId);
        stock.decreaseStock(quantity);
    }

    //재고 증가 처리
    @Override
    @Transactional
    public void increaseStock(Long saleProductId, int quantity){
        Stock stock = findStockBySaleProductId(saleProductId);
        stock.increaseStock(quantity);
    }

    // 재고 확인
    @Override
    @Transactional(readOnly = true)
    public boolean checkStock(Long saleProductId, int quantity){
        Stock stock = findStockBySaleProductId(saleProductId);
        return stock.hasStock(quantity);
    }

    //판매 상품ID로 재고 조회
    private Stock findStockBySaleProductId(Long saleProductId){
        return stockRepository.findBySaleProduct_Id(saleProductId)
                .orElseThrow(() -> new DomainException(ErrorType.STOCK_NOT_FOUND));
    }
}
