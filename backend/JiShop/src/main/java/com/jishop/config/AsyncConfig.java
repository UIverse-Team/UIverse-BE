package com.jishop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
@EnableScheduling
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        // Spring 제공 스레드 풀 정의
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 기본 스레드 수(유지되는 최소 스레드 수)
        executor.setCorePoolSize(5);
        // 최대 스레드 수(동시 실행 가능한 스레드 수)
        executor.setMaxPoolSize(10);
        // 작업을 큐에 최대로 대기시킬수 있는 허용량
        executor.setQueueCapacity(100);
        // 생성된 스레드 이름
        executor.setThreadNamePrefix("TaskExecutor-");
        // 쓰레드 풀 초기화 후 리턴
        executor.initialize();
        return executor;
    }
}
