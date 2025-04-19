package com.jishop.product.domain.embed;

import com.jishop.category.domain.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryInfo {

    @Column(name = "l_cat_id", nullable = false)
    private Long lCatId;
    @Column(name = "m_cat_id")
    private Long mCatId;
    @Column(name = "s_cat_id")
    private Long sCatId;

    public CategoryInfo(Long lCatId, Long mCatId, Long sCatId) {
        this.lCatId = lCatId;
        this.mCatId = mCatId;
        this.sCatId = sCatId;
    }
}