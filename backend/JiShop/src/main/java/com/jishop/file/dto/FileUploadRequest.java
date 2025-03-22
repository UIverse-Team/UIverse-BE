package com.jishop.file.dto;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import org.springframework.web.multipart.MultipartFile;

public record FileUploadRequest(
        String type,
        String usage,
        MultipartFile file
) {
    public static FileUploadRequest bind(String type, String usage, MultipartFile file) {
        return new FileUploadRequest(type, usage, file);
    }

    public FileUploadRequest {
        switch (type) {
            case "image" -> imageValidation(file);
            default -> throw new DomainException(ErrorType.TYPE_NOT_FOUND);
        }
    }

    private void imageValidation(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.matches("(?i).+\\.(jpg|jpeg|png|gif)$")) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다.");
        }

        // Content-Type 체크
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        // 용량 체크 (5MB 이하)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("최대 5MB까지 업로드 가능합니다.");
        }
    }

    public FileCreate toModel() {
        return FileCreate.builder()
                .file(file)
                .usage(usage)
                .type(type)
                .build();
    }
}
