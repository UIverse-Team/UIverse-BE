package com.jiseller.product.dto;

import com.jiseller.product.domain.DiscountStatus;
import com.jiseller.product.domain.Labels;
import com.jiseller.product.domain.Product;
import com.jiseller.product.domain.SaleStatus;
import com.jiseller.product.domain.embed.CategoryInfo;
import com.jiseller.product.domain.embed.ImageUrl;
import com.jiseller.product.domain.embed.ProductInfo;
import com.jiseller.product.domain.embed.Status;
import com.jiseller.store.domain.Store;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record ProductRegistrationRequest(
        @NotBlank(message = "상품명은 필수입니다")
        String name,
        @NotBlank(message = "StoreSeq는 필수입니다")
        Long mallSeq,
        @NotBlank(message = "제조 일자는 필수입니다")
        LocalDateTime manufactureDate,
        String description,
        @NotBlank(message = "판매가는 필수입니다")
        @Min(value = 0, message = "판매가는 0원 이상이어야 합니다")
        Integer originPrice,
        @NotBlank(message = "할인가는 필수입니다")
        @Min(value = 0, message = "할인가는 0원 이상이어야 합니다")
        Integer discountPrice,

        @NotBlank(message = "lCatId는 필수입니다")
        Long lCatId,
        Long mCatId,
        Long sCatId,

        @NotBlank(message = "MainImage는 필수입니다")
        MultipartFile mainImage,
        List<MultipartFile> additionalImages,
        @NotBlank(message = "DetailImage는 필수입니다")
        MultipartFile detailImage,

        @NotBlank(message = "상품상태는 필수입니다")
        SaleStatus saleStatus,
        @NotBlank(message = "라벨은 필수입니다")
        Labels labels,
        @NotBlank(message = "할인상태는 필수입니다")
        DiscountStatus discountStatus,

        @Min(value = 0, message = "재고 수량은 0개 이상이어야 합니다")
        int stockQuantity
) {
    public Product toEntity(Store store) {
        return Product.builder()
                .productInfo(createProductInfo(store))
                .categoryInfo(createCategoryInfo())
                .image(createImageUrl())
                .status(createStatus())
                .wishListCount(0)
                .productViewCount(0)
                .build();
    }

    private ProductInfo createProductInfo(Store store) {
        return ProductInfo.builder()
                .name(this.name)
                .mallSeq(store.getMallSeq())
                .manufactureDate(this.manufactureDate)
                .brand(store.getMallName())
                .description(this.description)
                .originPrice(this.originPrice)
                .discountPrice(this.discountPrice)
                .discountRate(calculateDiscountRate())
                .build();
    }

    private CategoryInfo createCategoryInfo() {
        return new CategoryInfo(lCatId, mCatId, sCatId);
    }

    private ImageUrl createImageUrl() {
        // 추후에 파일 업로드 기능 추가 후 변경 예정
        return new ImageUrl(
                "https://main_test.jpg", // 임시 URL
                null,
                null,
                null,
                null,
                "https://detail_test.jpg"
        );
    }

    private Status createStatus() {
        return Status.builder()
                .secret(false)  // 추후 백오피스 관리 예정
                .saleStatus(this.saleStatus)
                .labels(this.labels)
                .discountStatus(this.discountStatus)
                .isDiscount(false)  // 추후 백오피스 관리 예정
                .build();
    }

    private int calculateDiscountRate() {
        if (originPrice <= 0) return 0;
        return (int) (((double) (originPrice - discountPrice) / originPrice) * 100);
    }
}
