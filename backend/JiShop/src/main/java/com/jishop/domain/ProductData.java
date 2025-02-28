package com.jishop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductData {
    @Id
    private Long id;
    private String botId;
    private String storeId;
    private String productName;
    private Integer originalPrice;
    private String dcPrice;
    private String productImgUrl;
    private String productCode;
    private String detailUrl;
    private String description;
    private boolean qnaOptionBtn;
    private boolean deleteFlag; // delete SQL 예약어 충돌 방지
    private String category;
    private boolean similarOptionBtn;
    private boolean onSales;
    private String labels;
    @Column(columnDefinition = "BLOB")
    private Object sns;
    private LocalDateTime publishedAt;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String secret;
    private String ecField;
    private String guardYn;
    @Column(columnDefinition = "BLOB")
    private String productSns;

    public ProductData(
            Long id, String botId, String storeId, String productName, Integer originalPrice, String dcPrice,
            String productImgUrl, String productCode, String detailUrl, String description, boolean qnaOptionBtn,
            boolean deleteFlag, String category, boolean similarOptionBtn, boolean onSales, String labels,
            Object sns, LocalDateTime publishedAt, String createdBy, String updatedBy,
            LocalDateTime createdAt, LocalDateTime updatedAt, String secret, String ecField, String guardYn,
            String productSns
    ) {
        this.id = id;
        this.botId = botId;
        this.storeId = storeId;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.dcPrice = dcPrice;
        this.productImgUrl = productImgUrl;
        this.productCode = productCode;
        this.detailUrl = detailUrl;
        this.description = description;
        this.qnaOptionBtn = qnaOptionBtn;
        this.deleteFlag = deleteFlag;
        this.category = category;
        this.similarOptionBtn = similarOptionBtn;
        this.onSales = onSales;
        this.labels = labels;
        this.sns = sns;
        this.publishedAt = publishedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.secret = secret;
        this.ecField = ecField;
        this.guardYn = guardYn;
        this.productSns = productSns;
    }
}