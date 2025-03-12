package com.jishop.crawler.dto;

import java.time.LocalDateTime;

public class ProductCrawlerData {
    private String categoryId;
    private String lcatId;
    private String mcatId;
    private String scatId;
    private String storeSeq;
    private String name;
    private String description;
    private Integer originPrice;
    private Integer discountPrice;
    private String manufactureDate; // JSON 직렬화/역직렬화를 위해 String으로 처리
    private Boolean secret;
    private String saleStatus; // Enum 타입 대신 문자열로 처리
    private String discountStatus; // Enum 타입 대신 문자열로 처리
    private Boolean isDiscount;
    private String brand;
    private Double rate;
    private Integer reviewCount;
    private Integer likeCount;
    private String labels; // Enum 타입 대신 문자열로 처리
    private Boolean deleteFlag;
    private String mainImage;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String detailImage;

    // Getters and Setters
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getLcatId() { return lcatId; }
    public void setLcatId(String lcatId) { this.lcatId = lcatId; }

    public String getMcatId() { return mcatId; }
    public void setMcatId(String mcatId) { this.mcatId = mcatId; }

    public String getScatId() { return scatId; }
    public void setScatId(String scatId) { this.scatId = scatId; }

    public String getStoreSeq() { return storeSeq; }
    public void setStoreSeq(String storeSeq) { this.storeSeq = storeSeq; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getOriginPrice() { return originPrice; }
    public void setOriginPrice(Integer originPrice) { this.originPrice = originPrice; }

    public Integer getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(Integer discountPrice) { this.discountPrice = discountPrice; }

    public String getManufactureDate() { return manufactureDate; }
    public void setManufactureDate(LocalDateTime manufactureDate) { this.manufactureDate = manufactureDate.toString(); }

    public Boolean getSecret() { return secret; }
    public void setSecret(Boolean secret) { this.secret = secret; }

    public String getSaleStatus() { return saleStatus; }
    public void setSaleStatus(String saleStatus) { this.saleStatus = saleStatus; }

    public String getDiscountStatus() { return discountStatus; }
    public void setDiscountStatus(String discountStatus) { this.discountStatus = discountStatus; }

    public Boolean getIsDiscount() { return isDiscount; }
    public void setIsDiscount(Boolean isDiscount) { this.isDiscount = isDiscount; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public String getLabels() { return labels; }
    public void setLabels(String labels) { this.labels = labels; }

    public Boolean getDeleteFlag() { return deleteFlag; }
    public void setDeleteFlag(Boolean deleteFlag) { this.deleteFlag = deleteFlag; }

    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }

    public String getImage1() { return image1; }
    public void setImage1(String image1) { this.image1 = image1; }

    public String getImage2() { return image2; }
    public void setImage2(String image2) { this.image2 = image2; }

    public String getImage3() { return image3; }
    public void setImage3(String image3) { this.image3 = image3; }

    public String getImage4() { return image4; }
    public void setImage4(String image4) { this.image4 = image4; }

    public String getDetailImage() { return detailImage; }
    public void setDetailImage(String detailImage) { this.detailImage = detailImage; }
}