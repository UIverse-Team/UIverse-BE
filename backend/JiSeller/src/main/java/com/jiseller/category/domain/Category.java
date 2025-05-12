package com.jiseller.category.domain;

import com.jiseller.common.util.BaseEntity;
import com.jiseller.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "category")
    private final List<Product> products = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "current_id", foreignKey = @ForeignKey(name = "fk_category_parent"))
    private Category parent;
    @Column(name = "current_id", nullable = false, unique = true)
    private Long currentId;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private final List<Category> children = new ArrayList<>();

    @Column(name = "whole_category_id", nullable = false, length = 200)
    private String wholeCategoryId;
    @Column(name = "whole_category_name", nullable = false, length = 200)
    private String wholeCategoryName;
    @Column(name = "level", nullable = false)
    private Integer level;
}
