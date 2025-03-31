package com.jishop.log;

import com.jishop.log.dto.PageViewLogDto;
import com.jishop.log.dto.ProductClickLogDto;
import com.jishop.log.dto.ReviewLogDto;
import com.jishop.log.dto.SearchLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class LogRepository {
    private final JdbcTemplate logJdbcTemplate;

    public LogRepository(@Qualifier("logJdbcTemplate") JdbcTemplate logJdbcTemplate) {
        this.logJdbcTemplate = logJdbcTemplate;
    }

    public List<PageViewLogDto> findRecentPageViewsByUserId(Long userId) {
        String sql = "SELECT id, user_id, page_url, visit_time, exit_time, duration_seconds " +
                "FROM page_view_logs " +
                "WHERE user_id = ? " +
                "ORDER BY visit_time DESC " +
                "LIMIT 10";

        return logJdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new PageViewLogDto(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("page_url"),
                        rs.getTimestamp("visit_time").toLocalDateTime(),
                        rs.getTimestamp("exit_time") != null ?
                                rs.getTimestamp("exit_time").toLocalDateTime() : null,
                        rs.getObject("duration_seconds", Integer.class)
                )
        );
    }

    public List<SearchLogDto> findRecentSearchesByUserId(Long userId) {
        String sql = "SELECT id, user_id, search_keyword, search_time " +
                "FROM search_logs " +
                "WHERE user_id = ? " +
                "ORDER BY search_time DESC " +
                "LIMIT 10";

        return logJdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new SearchLogDto(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("search_keyword"),
                        rs.getTimestamp("search_time").toLocalDateTime()
                )
        );
    }

    public List<ProductClickLogDto> findRecentProductClicksByUserId(Long userId) {
        String sql = "SELECT id, user_id, product_id, product_name, click_time " +
                "FROM product_click_logs " +
                "WHERE user_id = ? " +
                "ORDER BY click_time DESC " +
                "LIMIT 10";

        return logJdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new ProductClickLogDto(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getTimestamp("click_time").toLocalDateTime()
                )
        );
    }

    public List<ReviewLogDto> findRecentReviewViewsByUserId(Long userId) {
        String sql = "SELECT id, user_id, product_id, product_name, review_id, view_time " +
                "FROM review_logs " +
                "WHERE user_id = ? " +
                "ORDER BY view_time DESC " +
                "LIMIT 10";

        return logJdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new ReviewLogDto(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getLong("review_id"),
                        rs.getTimestamp("view_time").toLocalDateTime()
                )
        );
    }

    public int countTotalLogsByType(Long userId, String logType) {
        String sql = switch (logType) {
            case "pageView" -> "SELECT COUNT(*) FROM page_view_logs WHERE user_id = ?";
            case "search" -> "SELECT COUNT(*) FROM search_logs WHERE user_id = ?";
            case "productClick" -> "SELECT COUNT(*) FROM product_click_logs WHERE user_id = ?";
            case "reviewView" -> "SELECT COUNT(*) FROM review_logs WHERE user_id = ?";
            default -> throw new IllegalArgumentException("Invalid log type");
        };

        return logJdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
    }
}