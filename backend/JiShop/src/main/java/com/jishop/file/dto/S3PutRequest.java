package com.jishop.file.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
public class S3PutRequest {

    private final MultipartFile multipartFile;
    private final String key; // s3 파일 저장소 url
    private final long size;

    S3PutRequest(MultipartFile multipartFile, String key, long size) {
        this.multipartFile = multipartFile;
        this.key = key;
        this.size = size;
    }

    public static S3PutRequest from(FileCreate fileCreate) {
        MultipartFile file = fileCreate.getFile();
        String originalFilename = file.getOriginalFilename();
        String storedFilename = String.format("%s_%s", UUID.randomUUID().toString(), originalFilename);
        String key = String.format("%s/%s/%s", fileCreate.getUsage(), fileCreate.getType(), storedFilename);

        return new S3PutRequest(file, key, file.getSize());
    }

}
