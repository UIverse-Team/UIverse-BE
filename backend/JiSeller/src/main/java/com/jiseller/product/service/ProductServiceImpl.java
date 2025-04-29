package com.jiseller.product.service;

import com.jiseller.category.domain.Category;
import com.jiseller.category.repository.CategoryRepository;
import com.jiseller.common.exception.DomainException;
import com.jiseller.common.exception.ErrorType;
import com.jiseller.option.domain.Option;
import com.jiseller.option.repository.OptionRepository;
import com.jiseller.product.domain.Product;
import com.jiseller.product.dto.RegisterProductRequest;
import com.jiseller.product.repository.ProductRepository;
import com.jiseller.saleproduct.service.SaleProductService;
import com.jiseller.stock.domain.Stock;
import com.jiseller.stock.repository.StockRepository;
import com.jiseller.store.domain.Store;
import com.jiseller.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;
    private final OptionRepository optionRepository;

    private final SaleProductService saleProductService;

    @Override
    @Transactional
    public void registerProduct(final RegisterProductRequest registerProductRequest) {
        final Store store = storeRepository.findByMallSeq(registerProductRequest.mallSeq())
                .orElseThrow(() -> new DomainException(ErrorType.STORE_NOT_FOUND));
        final Category category = categoryRepository.findById(registerProductRequest.lCatId())
                .orElseThrow(() -> new DomainException(ErrorType.CATEGORY_NOT_FOUND ));
        final List<Long> optionIds = registerProductRequest.optionIds();
        final List<Option> options = optionRepository.findAllById(optionIds);
        if (options.size() != optionIds.size()) {
            throw new DomainException(ErrorType.OPTION_NOT_FOUND);
        }

        Product product = productRepository.save(registerProductRequest.toEntity(store));

        for (Long optionId : optionIds) {
            saleProductService.registerSaleProduct(product, optionId);
        }

        Stock stock = new Stock();
        stockRepository.save(stock);

    }
}
