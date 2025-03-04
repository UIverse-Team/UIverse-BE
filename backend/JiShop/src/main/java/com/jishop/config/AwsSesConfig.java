package com.jishop.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSesConfig {

    @Value("${aws.ses.accessKey}")
    private String accessKey;
    @Value("${aws.ses.secretKey}")
    private String secretKey;
    @Value("${aws.ses.region}")
    private String region;

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey); // 추후 yml 파일로 빼야할듯
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(region) // 사용하려는 리전 설정
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
