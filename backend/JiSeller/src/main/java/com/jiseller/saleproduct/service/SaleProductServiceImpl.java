package com.jiseller.saleproduct.service;

import com.jiseller.common.exception.DomainException;
import com.jiseller.common.exception.ErrorType;
import com.jiseller.option.domain.Option;
import com.jiseller.option.repository.OptionRepository;
import com.jiseller.product.domain.Product;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.saleproduct.util.OptionMap;
import com.jiseller.saleproduct.util.SaleProductMapper;
import com.jiseller.saleproduct.repository.SaleProductRepository;
import com.jiseller.stock.dto.SaleProductSpec;
import com.jiseller.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleProductServiceImpl implements SaleProductService {

    private final SaleProductRepository saleProductRepository;
    private final OptionRepository optionRepository;

    private final StockService stockService;

    @Override
    @Transactional
    public void registerSaleProduct(final Product product, final List<SaleProductSpec> optionStockSpecs) {
        final List<Long> optionIds = optionStockSpecs.stream().map(SaleProductSpec::optionId).toList();
        final List<Option> options = optionRepository.findAllById(optionIds);
        final OptionMap optionMap = OptionMap.from(options);
        if (optionMap.optionMap().size() != optionIds.size()) {
            throw new DomainException(ErrorType.OPTION_NOT_FOUND);
        }

        final List<SaleProduct> saleProducts = optionStockSpecs.stream()
                .map(spec -> {
                    Long optionId = spec.optionId();
                    Option option = optionMap.optionMap().get(optionId);
                    return SaleProductMapper.toEntity(product, option);
                })
                .toList();

        final List<SaleProduct> savedSaleProducts = saleProductRepository.saveAll(saleProducts);
        stockService.registerStocks(savedSaleProducts, optionStockSpecs);
    }
}