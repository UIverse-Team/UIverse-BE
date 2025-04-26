package com.jishop.product.domain;

import com.jishop.category.domain.Category;
import com.jishop.common.util.BaseEntity;
import com.jishop.product.domain.embed.CategoryInfo;
import com.jishop.product.domain.embed.ImageUrl;
import com.jishop.product.domain.embed.ProductInfo;
import com.jishop.product.domain.embed.Status;
import com.jishop.productscore.domain.ProductScore;
import com.jishop.saleproduct.domain.SaleProduct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Embedded
    private ProductInfo productInfo;
    @Embedded
    private CategoryInfo categoryInfo;
    @Embedded
    private ImageUrl image;
    @Embedded
    private Status status;

    @Column(name = "wish_list_count", nullable = false, columnDefinition = "int default 0")
    private int wishListCount;
    @Column(name = "product_view_count", nullable = false, columnDefinition = "int default 0")
    private int productViewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private final List<SaleProduct> saleProducts = new ArrayList<>();
    @OneToOne(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private ProductScore productScore;

    public Product(ProductInfo productInfo, CategoryInfo categoryInfo, Status status, ImageUrl image,
            Category category, int wishListCount, int productViewCount
    ) {
        this.productInfo = productInfo;
        this.categoryInfo = categoryInfo;
        this.status = status;
        this.image = image;
        this.category = category;
        this.wishListCount = wishListCount;
        this.productViewCount = productViewCount;
    }

    public void incrementWishCount() {
        this.wishListCount++;
    }

    public void decrementWishCount() {
        if (this.wishListCount > 0) { this.wishListCount--; }
    }

    public void setProductScore(ProductScore productScore) {
        this.productScore = productScore;
        if (productScore != null && productScore.getProduct() != this) { productScore.setProduct(this); }
    }
}

