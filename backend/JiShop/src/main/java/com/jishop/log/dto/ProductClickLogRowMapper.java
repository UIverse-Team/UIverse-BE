package com.jishop.log.dto;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProductClickLogRowMapper implements RowMapper<ProductClickLogDto> {
    @Override
    public ProductClickLogDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new ProductClickLogDto(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("product_id"),
                rs.getString("product_name"),
                rs.getObject("click_time", LocalDate.class)
        );
    }
}
