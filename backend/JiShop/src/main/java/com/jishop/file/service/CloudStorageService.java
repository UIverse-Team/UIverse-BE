package com.jishop.file.service;

import com.jishop.file.dto.FileCreate;
import com.jishop.file.dto.S3PutRequest;

public interface CloudStorageService {
    S3PutRequest upload(FileCreate fileCreate);
}
