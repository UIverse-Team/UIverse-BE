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

    int stockNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saleProduct_id")
    SaleProduct saleProduct;

    //재고 감소 처리
    public void decreaseStock(int quantity) {
        if(this.stockNumber < quantity) {
            throw new DomainException(ErrorType.INSUFFICIENT_STOCK);
        }
        this.stockNumber -= quantity;
    }

    //재고 증가 처리
    public void increaseStock(int quantity) {
        this.stockNumber += quantity;
    }

    //재고 확인
    public boolean hasStock(int quantity){
        return this.stockNumber >= quantity;
    }
}

