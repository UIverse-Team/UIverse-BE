package com.jishop.batch;


import com.jishop.domain.ProductData;
import com.jishop.dto.productdata.ProductDataRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ProductDataBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemReader<ProductDataRequest> productDataReader;
    private final ItemProcessor<ProductDataRequest, ProductData> productDataProcessor;
    private final ItemWriter<ProductData> productDataWriter;

    @Bean
    public Job productDataImportJob() {
        return new JobBuilder("productDataImportJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(productDataImportStep())
                .end()
                .build();
    }

    @Bean
    public Step productDataImportStep() {
        return new StepBuilder("productDataImportStep", jobRepository)
                .<ProductDataRequest, ProductData>chunk(1000, transactionManager)
                .reader(productDataReader)
                .processor(productDataProcessor)
                .writer(productDataWriter)
                .build();
    }
}
