package com.jishop.dto.productdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jishop.domain.ProductData;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 지혜 Rest API 상품 리스트
    "id": 998,
    "botId": "41",
    "storeId": "41",
    "productName": "[샘플용] 카시오 전자사전 EW-H6000",
    "originalPrice": null, (할인 가격을 표현할때 원가를 표시 하고 싶을때 들어감)
    "dcPrice": "252000", (실제 고객에게 제공되는 가격)
    "productImgUrl": "https://shopping-phinf.pstatic.net/main_8352911/83529118666.1.jpg", (상품 이미지)
    "productCode": null,
    "detailUrl": "https://smartstore.naver.com/main/products/5984619178", (상품 링크)
    "description": "카시오 전자사전 EW-H6000", (상품 설명)
    "qnaOptionBtn": true,
    "delete": false,
    "category": "custom", (상품 카테고리)
    "similarOptionBtn": true,
    "onSales": true,
    "labels": null,
    "sns": [

    ], (상품에 연결된 sns 링크들)
    "published_at": "2025-02-20T07:23:44.000Z",
    "created_by": null,
    "updated_by": null,
    "created_at": "2025-02-20T07:23:44.000Z",
    "updated_at": "2025-02-20T07:23:44.000Z",
    "secret": null,
    "ecField": null,
    "guardYn": null,
    "product_sns": "[]"
 */

public record ProductDataRequest(
        Long id,
        String botId,
        String storeId,
        String productName,
        Integer originalPrice,
        String dcPrice,
        String productImgUrl,
        String productCode,
        String detailUrl,
        String description,
        boolean qnaOptionBtn,
        @JsonProperty("delete")
        boolean deleteFlag,
        String category,
        boolean similarOptionBtn,
        boolean onSales,
        String labels,
        List<SnsInfo> sns,
        @JsonProperty("published_at")
        LocalDateTime publishedAt,
        @JsonProperty("created_by")
        String createdBy,
        @JsonProperty("updated_by")
        String updatedBy,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("updated_at")
        LocalDateTime updatedAt,
        String secret,
        String ecField,
        String guardYn,
        @JsonProperty("product_sns")
        String productSns
) {
    public ProductData toEntity() {
        return new ProductData(
                this.id,
                this.botId,
                this.storeId,
                this.productName,
                this.originalPrice,
                this.dcPrice,
                this.productImgUrl,
                this.productCode,
                this.detailUrl,
                this.description,
                this.qnaOptionBtn,
                this.deleteFlag,
                this.category,
                this.similarOptionBtn,
                this.onSales,
                this.labels,
                this.sns,
                this.publishedAt,
                this.createdBy,
                this.updatedBy,
                this.createdAt,
                this.updatedAt,
                this.secret,
                this.ecField,
                this.guardYn,
                this.productSns
        );
    }
    @Embeddable
    public record SnsInfo(
            String platform,
            String url
    ) {
    }
}
