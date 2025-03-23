package com.jishop.file.service;

import com.jishop.file.dto.FileCreate;
import com.jishop.file.dto.S3PutRequest;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3BucketServiceImpl implements CloudStorageService {

    private final S3Template s3Template;
    private String BUCKET_NAME = "uiverse-bucket";

    @Override
    public S3PutRequest upload(FileCreate fileCreate) {
        S3PutRequest s3PutRequest = S3PutRequest.from(fileCreate);

        try (InputStream inputStream = fileCreate.getFile().getInputStream()) {
            s3Template.upload(BUCKET_NAME,
                    s3PutRequest.getKey(),
                    inputStream);

            return s3PutRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
