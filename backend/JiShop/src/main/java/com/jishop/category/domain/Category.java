package com.jishop.category.domain;

import com.jishop.common.util.BaseEntity;
import com.jishop.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    @Setter // parent 넣으려고 넣어줬다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "current_id", foreignKey = @ForeignKey(name = "fk_category_parent"))
    private Category parent;

    @Column(name = "current_id", nullable = false, unique = true)
    private Long currentId;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private final List<Category> children = new ArrayList<>();

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "whole_category_id", nullable = false, length = 200)
    private String wholeCategoryId;

    @Column(name = "whole_category_name", nullable = false, length = 200)
    private String wholeCategoryName;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Builder
    public Category(
            Category parent,
            Long currentId,
            String name,
            String wholeCategoryId,
            String wholeCategoryName,
            Integer level
    ){
        this.parent = parent;
        this.currentId = currentId;
        this.name = name;
        this.wholeCategoryId = wholeCategoryId;
        this.wholeCategoryName = wholeCategoryName;
        this.level = level;
    }

    public void addChildCategory(Category child) {
        this.children.add(child);
        child.setParent(this);
    }
}