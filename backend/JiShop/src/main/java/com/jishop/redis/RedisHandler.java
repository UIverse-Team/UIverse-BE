package com.jishop.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
