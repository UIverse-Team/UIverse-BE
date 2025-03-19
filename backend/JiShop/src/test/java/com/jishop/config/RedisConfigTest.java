package com.jishop.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisConfigTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @Test
    void testRedisTemplateBean() {
        assertNotNull(redisTemplate, "RedisTemplate 빈이 null");
    }

    @Test
    void testRedisConnectionFactoryBean() {
        assertNotNull(redisConnectionFactory, "RedisConnectionFactory 빈이 null");
    }

}