package com.jishop.image;

import com.jishop.review.domain.embed.ImageUrls;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3ImagesServiceImpl implements S3ImagesService{
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public List<String> upload(List<MultipartFile> files) {




        return List.of();
    }

    @Override
    public void delete(ImageUrls imageUrls) {


    }
}
