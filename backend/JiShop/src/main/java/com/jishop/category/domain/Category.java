package com.jishop.category.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "current_id", foreignKey = @ForeignKey(name = "fk_category_parent"))
    private Category parentId;

    @Column(name = "current_id", nullable = false, unique = true)
    private Long currentId;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "whole_category_id", nullable = false, length = 200)
    private String wholeCategoryId;

    @Column(name = "whole_category_name", nullable = false, length = 200)
    private String wholeCategoryName;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "last_level", nullable = false)
    private Boolean lastLevel;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "sell_blog_use", nullable = false)
    private Boolean sellBlogUse = true;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "juvenile_harmful", nullable = false)
    private Boolean juvenileHarmful = false;

    @Builder
    public Category(
            Category parentId,
            Long currentId,
            String name,
            String wholeCategoryId,
            String wholeCategoryName,
            Integer level,
            Boolean lastLevel,
            Boolean deleted,
            Boolean sellBlogUse,
            Integer sortOrder,
            Boolean juvenileHarmful
    ){
        this.parentId = parentId;
        this.currentId = currentId;
        this.name = name;
        this.wholeCategoryId = wholeCategoryId;
        this.wholeCategoryName = wholeCategoryName;
        this.level = level;
        this.lastLevel = lastLevel;
        this.deleted = deleted;
        this.sellBlogUse = sellBlogUse;
        this.sortOrder = sortOrder;
        this.juvenileHarmful = juvenileHarmful;
    }
}