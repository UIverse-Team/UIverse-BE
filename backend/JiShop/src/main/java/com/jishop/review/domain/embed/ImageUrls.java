package com.jishop.review.domain.embed;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
@NoArgsConstructor
public class ImageUrls {

    @Transient
    private final String PREFIX = "https://d5hvuqrbdt6lu.cloudfront.net/";

    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

    public ImageUrls(List<String> images) {

        if (images.size() > 5)
            throw new IllegalArgumentException("이미지는 5개를 초과할 수 없습니다.");

        this.image1 = images.size() > 0 ? images.get(0) : null;
        this.image2 = images.size() > 1 ? images.get(1) : null;
        this.image3 = images.size() > 2 ? images.get(2) : null;
        this.image4 = images.size() > 3 ? images.get(3) : null;
        this.image5 = images.size() > 4 ? images.get(4) : null;
    }

    public List<String> getImages() {
        return Stream.of(image1, image2, image3, image4, image5)
                .filter(Objects::nonNull)
                .map(img -> PREFIX + img)
                .collect(Collectors.toList());
    }
}

