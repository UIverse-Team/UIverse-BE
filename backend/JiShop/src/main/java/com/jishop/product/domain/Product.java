package com.jishop.product.domain;

import com.jishop.category.domain.Category;
import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    // 상품 정보
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "mall_seq",  nullable = false)
    private String mallSeq;
    @Column(name = "manufacture_date", nullable = false)
    private LocalDateTime manufactureDate;
    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "origin_price", nullable = false)
    private int originPrice;
    @Column(name = "discount_price", nullable = false)
    private int discountPrice;
    @Column(name = "discount_rate", nullable = false)
    private BigDecimal discountRate;

    // 상품 상태
    @Column(name = "secret", nullable = false)
    private Boolean secret;
    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status", nullable = false)
    private SaleStatus saleStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "labels", length = 50)
    private Labels labels;
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_status", nullable = false)
    private DiscountStatus discountStatus;

    // "오늘의 특가" 구분 필드값
    @Column(name = "is_discount", nullable = false)
    private Boolean isDiscount;

    @Column(name = "wish_list_count", nullable = false, columnDefinition = "int default 0")
    private int wishListCount;
    @Column(name = "product_view_count", nullable = false, columnDefinition = "int default 0")
    private int productViewCount;

    @Column(name = "main_image", nullable = false)
    private String mainImage;
    @Column(name = "image1")
    private String image1;
    @Column(name = "image2")
    private String image2;
    @Column(name = "image3")
    private String image3;
    @Column(name = "image4")
    private String image4;
    @Column(name = "detail_image")
    private String detailImage;

    // 카테고리 분류
    @Column(name = "l_cat_id",  nullable = false)
    private String lCatId;
    @Column(name = "m_cat_id")
    private String mCatId;
    @Column(name = "s_cat_id")
    private String sCatId;

    @Builder
    public Product(Category category, String lCatId, String mCatId, String sCatId,
            String mallSeq, String name, String description, int originPrice, int discountPrice,
            BigDecimal discountRate, LocalDateTime manufactureDate, Boolean secret, SaleStatus saleStatus,
            DiscountStatus discountStatus, Boolean isDiscount, String brand, int wishListCount, Labels labels,
            String mainImage, String image1, String image2, String image3, String image4, String detailImage,
            int productViewCount
    ) {
        this.category = category;
        this.lCatId = lCatId;
        this.mCatId = mCatId;
        this.sCatId = sCatId;
        this.mallSeq = mallSeq;
        this.name = name;
        this.description = description;
        this.originPrice = originPrice;
        this.discountPrice = discountPrice;
        this.discountRate = discountRate;
        this.manufactureDate = manufactureDate;
        this.secret = secret;
        this.saleStatus = saleStatus;
        this.discountStatus = discountStatus;
        this.isDiscount = isDiscount;
        this.brand = brand;
        this.wishListCount = wishListCount;
        this.labels = labels;
        this.mainImage = mainImage;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.detailImage = detailImage;
        this.productViewCount = productViewCount;
    }

    public void incrementWishCount() {
        this.wishListCount++;
    }

    public void decrementWishCount() {
        if (this.wishListCount > 0) { this.wishListCount--;}
    }
}

