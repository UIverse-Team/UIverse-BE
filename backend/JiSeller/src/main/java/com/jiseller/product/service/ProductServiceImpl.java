package com.jiseller.product.service;

import com.jiseller.category.repository.CategoryRepository;
import com.jiseller.common.exception.DomainException;
import com.jiseller.common.exception.ErrorType;
import com.jiseller.product.domain.Product;
import com.jiseller.product.dto.RegisterProductRequest;
import com.jiseller.product.repository.ProductRepository;
import com.jiseller.saleproduct.service.SaleProductService;
import com.jiseller.store.domain.Store;
import com.jiseller.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    private final SaleProductService saleProductService;

    @Override
    @Transactional
    public void registerProduct(final RegisterProductRequest registerProductRequest) {
        categoryRepository.findById(registerProductRequest.lCatId())
                .orElseThrow(() -> new DomainException(ErrorType.CATEGORY_NOT_FOUND));
        final Store store = storeRepository.findByMallSeq(registerProductRequest.mallSeq())
                .orElseThrow(() -> new DomainException(ErrorType.STORE_NOT_FOUND));

        final Product product = productRepository.save(registerProductRequest.toEntity(store));
        saleProductService.registerSaleProduct(product, registerProductRequest.saleProductSpecs());
    }
}
