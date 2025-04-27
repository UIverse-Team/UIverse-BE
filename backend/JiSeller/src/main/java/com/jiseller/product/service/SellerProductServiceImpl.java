package com.jiseller.product.service;

import com.jiseller.category.domain.Category;
import com.jiseller.category.repository.CategoryRepository;
import com.jiseller.common.exception.DomainException;
import com.jiseller.common.exception.ErrorType;
import com.jiseller.product.domain.Product;
import com.jiseller.product.domain.embed.Status;
import com.jiseller.product.dto.*;
import com.jiseller.product.repository.ProductRepository;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.saleproduct.repository.SaleProductRepository;
import com.jiseller.stock.domain.Stock;
import com.jiseller.stock.repository.StockRepository;
import com.jiseller.store.domain.Store;
import com.jiseller.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellerProductServiceImpl implements SellerProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SaleProductRepository saleProductRepository;
    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public Long registerProduct(final ProductRegistrationRequest productRegistrationRequest) {
        final Store store = storeRepository.findByMallSeq(productRegistrationRequest.mallSeq())
                .orElseThrow(() -> new DomainException(ErrorType.STORE_NOT_FOUND));
        final Category category = categoryRepository.findById(productRegistrationRequest.lCatId())
                .orElseThrow(() -> new DomainException(ErrorType.CATEGORY_NOT_FOUND ));

        Product product = productRegistrationRequest.toEntity(store);
        product = productRepository.save(product);

        SaleProduct saleProduct = SaleProduct.builder()
                .product(product)
                .name(product.getProductInfo().getName())
                .build();
        saleProduct = saleProductRepository.save(saleProduct);

        // 11. Stock 정보 생성 및 저장
        Stock stock = new Stock(stockQuantity, saleProduct);
        stockRepository.save(stock);

        return product.getId();
    }
}
