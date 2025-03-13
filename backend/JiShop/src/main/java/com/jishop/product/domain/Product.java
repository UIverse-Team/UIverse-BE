package com.jishop.product.domain;

import com.jishop.category.domain.Category;
import jakarta.persistence.*;
import com.jishop.common.util.BaseEntity;
import lombok.AccessLevel;
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

    @Column(name = "l_cat_id")
    private String lCatId;

    @Column(name = "m_cat_id")
    private String mCatId;

    @Column(name = "s_cat_id")
    private String sCatId;

    @Column(name = "store_seq")
    private String storeSeq;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "origin_price", nullable = false)
    private Integer originPrice;

    @Column(name = "discount_price", nullable = false)
    private Integer discountPrice;

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

    @Column(name = "is_discount")
    private Boolean isDiscount;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "rate", nullable = false)
    private Double rate;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "labels", length = 50)
    @Enumerated(EnumType.STRING)
    private Labels labels;

    @Column(name = "delete_flag", nullable = false)
    private Boolean deleteFlag;

    @Column(name = "main_image")
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

    public Product(Category category, String lCatId, String mCatId, String sCatId,
            String storeSeq, String name, String description, Integer originPrice,
            Integer discountPrice, LocalDateTime manufactureDate, Boolean secret, SaleStatus saleStatus,
            DiscountStatus discountStatus, Boolean isDiscount, String brand, Double rate, Integer reviewCount,
            Integer likeCount, Labels labels,Boolean deleteFlag,
            String mainImage, String image1, String image2, String image3, String image4, String detailImage
    ) {
        this.category = category;
        this.lCatId = lCatId;
        this.mCatId = mCatId;
        this.sCatId = sCatId;
        this.storeSeq = storeSeq;
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
        this.rate = rate;
        this.reviewCount = reviewCount;
        this.likeCount = likeCount;
        this.labels = labels;
        this.deleteFlag = deleteFlag;
        this.mainImage = mainImage;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.detailImage = detailImage;
    }
}

