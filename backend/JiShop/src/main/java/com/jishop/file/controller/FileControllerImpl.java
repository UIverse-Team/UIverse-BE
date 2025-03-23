package com.jishop.file.controller;

import com.jishop.file.dto.FileUploadRequest;
import com.jishop.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileControllerImpl {
    private final FileService fileService;

    /**
     *
     * @param mediaType: 이미지, 영상 등등 판별 용도
     * @param usage : 리뷰, 상품 등등 판별 용도
     * @param uploadFile: 이미지, 영상 등등
     * @return s3에 저장한 key값
     */
    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("type") String mediaType,
                                         @RequestParam("usage") String usage,
                                         @RequestParam(value = "file") MultipartFile uploadFile) {
        // todo: 추후 type, usage를 enum으로 관리하자
        FileUploadRequest fileUploadRequest = FileUploadRequest.bind(mediaType, usage, uploadFile);
        String key = fileService.upload(fileUploadRequest.toModel());

        return ResponseEntity.ok(key);
    }

}
