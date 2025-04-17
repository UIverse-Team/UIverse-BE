package com.log.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisHandler redisHandler;

    public Boolean setDuplicateInput(String key) {
        return redisHandler.excuteOperation(
                () -> redisHandler.getOpsForValue().setIfAbsent(key, "s", getTTLUntilMidnight()));
    }

    public void putClickCount(String key) {

        Boolean flag = redisHandler.excuteOperation(
                () -> redisHandler.getOpsForValue().setIfAbsent(key, 1L, Duration.ofDays(3)));

        // false면 redis에 값이 들어가있으니까. 값을 올려줘야 한다..
        if (Boolean.FALSE.equals(flag)) {
            redisHandler.excuteOperation(() -> redisHandler.getOpsForValue().increment(key, 1L));
        } else if (flag == null) {
            // 레디스 실패 시 처리
        }
    }

    private Duration getTTLUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay(); // 내일 00:00

        return Duration.between(now, midnight);
    }
}
