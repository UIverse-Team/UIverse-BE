package com.log.repository;

import com.log.dto.ProductClickRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class LogRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void addProductClick(ProductClickRequest request) {
        String sql = "INSERT INTO product_click_logs (user_id, product_id, product_name, click_time) " +
                "VALUES (:userId, :productId, :productName, :clickTime)";

        SqlParameterSource params = new BeanPropertySqlParameterSource(request);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Transactional
    public void increaseProductClickCount(ProductClickRequest request) {




    }
}
