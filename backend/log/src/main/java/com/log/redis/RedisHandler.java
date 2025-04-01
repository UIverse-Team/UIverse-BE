package com.log.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisHandler {

    private final RedisTemplate redisTemplate;

    public void expire(String key, Long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    public HashOperations getOpsForHash() {
        return redisTemplate.opsForHash();
    }

    public ValueOperations getOpsForValue() {
        return redisTemplate.opsForValue();
    }

    public <T> T excuteOperation(Supplier<T> command) {
        try {
            return command.get();
        } catch (Exception e) {
            log.warn("Redis 작업 오류 발생: {}", e.getMessage());
            return null;
        }
    }

    public boolean excuteOperation(Runnable command) {
        try {
            command.run();
            return true;
        } catch (Exception e) {
            log.warn("Redis 작업 오류 발생: {}", e.getMessage());
            return false;
        }
    }

}
