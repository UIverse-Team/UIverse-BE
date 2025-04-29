package com.jiseller.saleproduct.service;

import com.jiseller.option.domain.Option;
import com.jiseller.product.domain.Product;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.saleproduct.dto.RegisterSaleProductRequest;
import com.jiseller.saleproduct.repository.SaleProductRepository;
import com.jiseller.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleProductServiceImpl implements SaleProductService {

    private final SaleProductRepository saleProductRepository;
    private final StockService stockService;

    @Override
    @Transactional
    public void registerSaleProduct(final Product product, final List<Option> options, final int stockQuantity) {
        for (final Option option : options) {
            final SaleProduct saleProduct = saleProductRepository
                    .save(new RegisterSaleProductRequest(product, option).toEntity());

            stockService.registerStock(saleProduct, stockQuantity);
        }
    }
}
