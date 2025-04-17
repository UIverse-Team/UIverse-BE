package com.jishop.config;

import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestRedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return mock(RedisConnectionFactory.class);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());  // 이렇게 factory 연결
        return redisTemplate;
    }

    @Bean
    public RedissonClient redissonClient() {
        return mock(RedissonClient.class);
    }
}
