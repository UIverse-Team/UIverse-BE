package com.jishop.config;

import com.jishop.client.NaverApiClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
// Feign 클라이언트들을 스캔 및 등록
@EnableFeignClients(basePackageClasses = NaverApiClient.class)
public class FeignClientConfig {
    // 해당 패키지 내의 Feign 인터페이스들이 자동으로 등록 DI(의존성 주입)
}
