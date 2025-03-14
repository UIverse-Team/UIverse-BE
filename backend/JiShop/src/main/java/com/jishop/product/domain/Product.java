    package com.jishop.product.domain;

import com.jishop.category.domain.Category;
import com.jishop.common.util.BaseEntity;

import jakarta.persistence.*;

import com.jishop.store.domain.Store;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "l_cat_id",  nullable = false)
    private String lCatId;

    @Column(name = "m_cat_id")
    private String mCatId;

    @Column(name = "s_cat_id")
    private String sCatId;

    @Column(name = "mall_seq",  nullable = false)
    private String mallSeq;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "origin_price", nullable = false)
    private int originPrice;

    @Column(name = "discount_price", nullable = false)
    private int discountPrice;

    @Column(name = "manufacture_date", nullable = false)
    private LocalDateTime manufactureDate;

    @Column(name = "secret", nullable = false)
    private Boolean secret;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status", nullable = false)
    private SaleStatus saleStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_status", nullable = false)
    private DiscountStatus discountStatus;

    // "오늘의 특가" 구분 필드값
    @Column(name = "is_discount", nullable = false)
    private Boolean isDiscount;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "wish_list_count", nullable = false, columnDefinition = "int default 0")
    private int wishListCount;

    @Column(name = "labels", length = 50)
    @Enumerated(EnumType.STRING)
    private Labels labels;

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

    @Column(name = "product_view_count", nullable = false, columnDefinition = "int default 0")
    private int productViewCount;

    public Product(Category category, String lCatId, String mCatId, String sCatId,
            String mallSeq, String name, String description, int originPrice,
            int discountPrice, LocalDateTime manufactureDate, Boolean secret, SaleStatus saleStatus,
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
}

