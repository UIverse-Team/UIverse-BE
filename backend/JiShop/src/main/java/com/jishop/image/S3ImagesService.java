package com.jishop.image;

import com.jishop.review.domain.embed.ImageUrls;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3ImagesService {
    List<String> upload(List<MultipartFile> files);
    void delete(ImageUrls imageUrls);
}
