package com.log.service.impl;

import com.log.dto.PageViewRequest;
import com.log.dto.ProductClickRequest;
import com.log.dto.ReviewRequest;
import com.log.dto.SearchRequest;
import com.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Long addPageLog(PageViewRequest request) {

        String sql = "INSERT INTO page_view_logs (user_id, page_url, visit_time) " +
                "VALUES (:userId, :pageUrl, :visitTime)";

        SqlParameterSource params = new BeanPropertySqlParameterSource(request);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void updatePageLog(PageViewRequest pageRequest, Long id) {
        String sql = "UPDATE page_view_logs SET exit_time = :exitTime, duration_seconds = :durationSeconds WHERE id = :id";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        mapSqlParameterSource.addValue("id", id);
        mapSqlParameterSource.addValue("exitTime", pageRequest.exitTime());
        mapSqlParameterSource.addValue("durationSeconds", pageRequest.durationSeconds());

        namedParameterJdbcTemplate.update(sql, mapSqlParameterSource);
    }

    @Override
    public void addSearchLog(SearchRequest request) {
        String sql = "INSERT INTO search_logs (user_id, search_keyword, search_time) " +
                "VALUES (:userId, :searchKeyword, :searchTime)";

        SqlParameterSource params = new BeanPropertySqlParameterSource(request);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void addProductClick(ProductClickRequest request) {
        String sql = "INSERT INTO product_click_logs (user_id, product_id, product_name, click_time) " +
                "VALUES (:userId, :productId, :productName, :clickTime)";

        SqlParameterSource params = new BeanPropertySqlParameterSource(request);
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public void addReviewLog(ReviewRequest request) {
        String sql = "INSERT INTO review_logs (user_id, product_id, product_name, review_id, view_time) " +
                "VALUES (:userId, :productId, :productName, :reviewId, :viewTime)";

        SqlParameterSource params = new BeanPropertySqlParameterSource(request);
        namedParameterJdbcTemplate.update(sql, params);
    }
}
