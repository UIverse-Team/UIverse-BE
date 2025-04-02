package com.jishop.redis;

import com.jishop.file.dto.S3PutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisHandler redisHandler;

    public String putImageKey(S3PutRequest s3PutRequest) {

        String redisKey = s3PutRequest.getKey();

        // todo: 레디스 저장하는 거 map으로 관리하자.
        if (redisHandler.excuteOperation(() -> redisHandler.getOpsForHash().put(redisKey, "saved", "false"))) {
            redisHandler.expire(redisKey, 1L, TimeUnit.DAYS);
            return redisKey;
        }

        return "failed";
    }
}
