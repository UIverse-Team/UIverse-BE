package com.jishop.file.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class FileCreate {
    private String type;
    private String usage;
    private MultipartFile file;

    @Builder
    public FileCreate(String type, String usage, MultipartFile file) {
        this.type = type;
        this.usage = usage;
        this.file = file;
    }
}
