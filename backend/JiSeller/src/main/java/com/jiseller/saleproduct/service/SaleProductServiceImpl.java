package com.jiseller.saleproduct.service;

import com.jiseller.common.exception.DomainException;
import com.jiseller.common.exception.ErrorType;
import com.jiseller.option.domain.Option;
import com.jiseller.option.repository.OptionRepository;
import com.jiseller.product.domain.Product;
import com.jiseller.product.repository.ProductRepository;
import com.jiseller.saleproduct.dto.RegisterSaleProductRequest;
import com.jiseller.saleproduct.repository.SaleProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaleProductServiceImpl implements SaleProductService {

    private final OptionRepository optionRepository;
    private final SaleProductRepository saleProductRepository;

    @Override
    @Transactional
    public void registerSaleProduct(final Product product, final Long optionId) {


        saleProductRepository.save(new RegisterSaleProductRequest(product, option));
    }
}
