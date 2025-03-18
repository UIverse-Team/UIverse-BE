package com.jishop.product.repository;

import com.jishop.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //TODO
    // 1.추후 상품 데이터 건수가 많아질 경우 DB에 할인율컬럼 추가
    @Query("SELECT p FROM Product p ORDER BY ((p.originPrice - p.discountPrice) / p.originPrice) DESC")
    Page<Product> findAllOrderByDiscountRateDesc(Pageable pageable);
}
