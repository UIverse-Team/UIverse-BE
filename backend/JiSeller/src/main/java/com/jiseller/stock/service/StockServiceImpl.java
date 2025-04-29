package com.jiseller.stock.service;

import com.jiseller.stock.dto.SaleProductSpec;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.stock.domain.Stock;
import com.jiseller.stock.dto.RegisterStockRequest;
import com.jiseller.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void registerStocks(final List<SaleProduct> saleProducts, final List<SaleProductSpec> optionStockSpecs) {
        List<Stock> stocks = IntStream.range(0, saleProducts.size())
                .mapToObj(i -> new RegisterStockRequest(
                        optionStockSpecs.get(i).stockQuantity(), saleProducts.get(i)).toEntity()).toList();

        stockRepository.saveAll(stocks);
    }
}
