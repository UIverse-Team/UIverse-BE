package com.jishop.batch;

import com.jishop.domain.ProductData;
import com.jishop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDataWriter implements ItemWriter<ProductData> {

    private final ProductRepository productRepository;

    @Override
    public void write(Chunk<? extends ProductData> chunk) {
        productRepository.saveAll(chunk.getItems());
    }
}
