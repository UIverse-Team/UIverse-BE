package com.jishop.category.domain;

import com.jishop.common.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @Column(length = 20)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
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

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 생성자
    public Category(
            String id,
            Category parent,
            String name,
            String wholeCategoryId,
            String wholeCategoryName,
            Integer level,
            Boolean lastLevel,
            Boolean deleted,
            Boolean sellBlogUse,
            Integer sortOrder,
            Boolean juvenileHarmful,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.wholeCategoryId = wholeCategoryId;
        this.wholeCategoryName = wholeCategoryName;
        this.level = level;
        this.lastLevel = lastLevel;
        this.deleted = deleted;
        this.sellBlogUse = sellBlogUse;
        this.sortOrder = sortOrder;
        this.juvenileHarmful = juvenileHarmful;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 부모 설정을 위한 setter
    public void setParent(Category parent) {
        this.parent = parent;
    }
}
