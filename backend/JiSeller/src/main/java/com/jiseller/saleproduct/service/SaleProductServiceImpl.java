package com.jiseller.saleproduct.service;

import com.jiseller.common.exception.DomainException;
import com.jiseller.common.exception.ErrorType;
import com.jiseller.option.domain.Option;
import com.jiseller.option.repository.OptionRepository;
import com.jiseller.product.domain.Product;
import com.jiseller.stock.dto.SaleProductSpec;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.saleproduct.dto.RegisterSaleProductRequest;
import com.jiseller.saleproduct.repository.SaleProductRepository;
import com.jiseller.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleProductServiceImpl implements SaleProductService {

    private final SaleProductRepository saleProductRepository;
    private final OptionRepository optionRepository;
    private final StockService stockService;

    @Override
    @Transactional
    public void registerSaleProduct(final Product product, final List<SaleProductSpec> optionStockSpecs) {
        List<Long> optionIds = optionStockSpecs.stream().map(SaleProductSpec::optionId).toList();
        Map<Long, Option> optionMap = optionRepository.findAllById(optionIds).stream()
                .collect(Collectors.toMap(Option::getId, option -> option));
        if (optionMap.size() != optionIds.size()) {
            throw new DomainException(ErrorType.OPTION_NOT_FOUND);
        }

        List<SaleProduct> saleProducts = new ArrayList<>();
        for (SaleProductSpec spec : optionStockSpecs) {
            Option option = optionMap.get(spec.optionId());
            saleProducts.add(new RegisterSaleProductRequest(product, option).toEntity());
        }

        List<SaleProduct> savedSaleProducts = saleProductRepository.saveAll(saleProducts);
        stockService.registerStocks(savedSaleProducts, optionStockSpecs);
    }
}
