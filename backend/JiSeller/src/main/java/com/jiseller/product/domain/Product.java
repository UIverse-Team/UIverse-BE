package com.jiseller.product.domain;

import com.jiseller.category.domain.Category;
import com.jiseller.common.util.BaseEntity;
import com.jiseller.product.domain.embed.CategoryInfo;
import com.jiseller.product.domain.embed.ImageUrl;
import com.jiseller.product.domain.embed.ProductInfo;
import com.jiseller.product.domain.embed.Status;
import com.jiseller.productscore.domain.ProductScore;
import com.jiseller.saleproduct.domain.SaleProduct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SaleProduct> saleProducts = new ArrayList<>();
    @OneToOne(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private ProductScore productScore;

    @Builder
    public Product(ProductInfo productInfo, CategoryInfo categoryInfo, ImageUrl image, Status status,
            int wishListCount, int productViewCount
    ) {
        this.productInfo = productInfo;
        this.categoryInfo = categoryInfo;
        this.status = status;
        this.image = image;
        this.wishListCount = wishListCount;
        this.productViewCount = productViewCount;
    }
}
