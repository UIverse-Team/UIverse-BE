package com.jishop.file.service;

import com.jishop.file.dto.FileCreate;
import com.jishop.file.dto.S3PutRequest;
import com.jishop.image.service.ImageService;
import com.jishop.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final RedisService redisService;
    private final ImageService imageService;
    private final CloudStorageService cloudStorageService;

    @Override
    public String upload(FileCreate fileCreate) {
        S3PutRequest s3PutRequest = cloudStorageService.upload(fileCreate);
        String key = redisService.putImageKey(s3PutRequest);

        // 레디스 실패시 db에 저장한다.
        if("failed".equals(key)){
            imageService.addTempKey(s3PutRequest);
            return s3PutRequest.getKey();
        }

        return key;
    }
}
