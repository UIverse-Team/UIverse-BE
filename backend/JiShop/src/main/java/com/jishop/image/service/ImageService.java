package com.jishop.image.service;

import com.jishop.file.dto.S3PutRequest;

public interface ImageService {
    void addTempKey(S3PutRequest s3PutRequest);
}
