package com.jiseller.stock.domain;

import com.jiseller.common.util.BaseEntity;
import com.jiseller.saleproduct.domain.SaleProduct;
import com.jiseller.common.exception.DomainException;
import com.jiseller.common.exception.ErrorType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    public void decreaseStock(int quantity) {
        validateQuantity(quantity);
        if (this.quantity < quantity) {
            throw new DomainException(ErrorType.INSUFFICIENT_STOCK);
        }
        this.quantity -= quantity;
    }

    public void increaseStock(int quantity) {
        validateQuantity(quantity);
        this.quantity += quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new DomainException(ErrorType.INVALID_QUANTITY);
        }
    }
}
