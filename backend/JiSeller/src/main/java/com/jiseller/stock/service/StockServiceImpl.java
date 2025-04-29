package com.jiseller.stock.service;

import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.stock.dto.RegisterStockRequest;
import com.jiseller.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    public void registerStock(final SaleProduct saleProduct, final int stockQuantity) {
        stockRepository.save(new RegisterStockRequest(stockQuantity, saleProduct).toEntity());
    }
}
