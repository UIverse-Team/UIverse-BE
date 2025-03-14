package com.jishop.saleproduct.Repository;

import com.jishop.saleproduct.domain.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

    @Query("SELECT sp FROM SaleProduct sp " +
            "LEFT JOIN FETCH sp.product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH sp.option " +
            "LEFT JOIN FETCH sp.stock WHERE sp.id = :id")
    Optional<SaleProduct> findByIdWithAllDetails(@Param("id") Long id);

    @Query("SELECT sp FROM SaleProduct sp " +
            "LEFT JOIN FETCH sp.product p " +
            "LEFT JOIN FETCH p.category " +
            "LEFT JOIN FETCH sp.option " +
            "LEFT JOIN FETCH sp.stock WHERE sp.id IN :ids")
    List<SaleProduct> findAllByIdWithAllDetails(@Param("ids") List<Long> ids);
}
