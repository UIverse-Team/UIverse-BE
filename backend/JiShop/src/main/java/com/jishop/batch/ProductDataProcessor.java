package com.jishop.batch;

import com.jishop.domain.ProductData;
import com.jishop.dto.productdata.ProductDataRequest;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductDataProcessor implements ItemProcessor<ProductDataRequest, ProductData> {

    @Override
    public ProductData process(ProductDataRequest item) {
        return item.toEntity();
    }
}
