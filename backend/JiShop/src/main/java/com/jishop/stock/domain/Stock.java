package com.jishop.stock.domain;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.common.util.BaseEntity;
import com.jishop.saleproduct.domain.SaleProduct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "stocks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stock extends BaseEntity {

    int quantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_product_id")
    SaleProduct saleProduct;

    public Stock(int quantity, SaleProduct saleProduct) {
        validateQuantity(quantity);
        this.quantity = quantity;
        this.saleProduct = saleProduct;
    }

    //재고 감소
    public void decreaseStock(int quantity) {
        validateQuantity(quantity);
        if (this.quantity < quantity) {
            throw new DomainException(ErrorType.INSUFFICIENT_STOCK);
        }
        this.quantity -= quantity;
    }

    //재고 증가
    public void increaseStock(int quantity) {
        validateQuantity(quantity);
        this.quantity += quantity;
    }

    //재고가 있는지 확인
    public boolean hasStock(int quantity) {
        return this.quantity >= quantity;
    }

    //재고 검증
    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new DomainException(ErrorType.INVALID_QUANTITY);
        }
    }
}

