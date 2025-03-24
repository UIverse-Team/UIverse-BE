package com.jishop.image.service;

import com.jishop.file.dto.S3PutRequest;
import com.jishop.image.domain.Image;
import com.jishop.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public void addTempKey(S3PutRequest s3PutRequest) {
        Image image = new Image(s3PutRequest.getKey(), false);
        imageRepository.save(image);
    }
}
