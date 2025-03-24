package com.jishop.file.service;

import com.jishop.file.dto.FileCreate;

public interface FileService {
    String upload(FileCreate fileCreate);
}
