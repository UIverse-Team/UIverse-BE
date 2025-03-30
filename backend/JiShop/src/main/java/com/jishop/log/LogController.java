package com.jishop.log;

import com.jishop.log.dto.PageViewLogDto;
import com.jishop.log.dto.ProductClickLogDto;
import com.jishop.log.dto.ReviewLogDto;
import com.jishop.log.dto.SearchLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/logs")
public class LogController {
    private final LogService logService;

    @GetMapping("/pageviews/{userId}")
    public ResponseEntity<List<PageViewLogDto>> getRecentPageViews(@PathVariable Long userId) {
        return ResponseEntity.ok(logService.getUserRecentPageViews(userId));
    }

    @GetMapping("/searches/{userId}")
    public ResponseEntity<List<SearchLogDto>> getRecentSearches(@PathVariable Long userId) {
        return ResponseEntity.ok(logService.getUserRecentSearches(userId));
    }

    @GetMapping("/productclicks/{userId}")
    public ResponseEntity<List<ProductClickLogDto>> getRecentProductClicks(@PathVariable Long userId) {
        return ResponseEntity.ok(logService.getUserRecentProductClicks(userId));
    }

    @GetMapping("/reviewviews/{userId}")
    public ResponseEntity<List<ReviewLogDto>> getRecentReviewViews(@PathVariable Long userId) {
        return ResponseEntity.ok(logService.getUserRecentReviewViews(userId));
    }

    @GetMapping("/count/{userId}/{logType}")
    public ResponseEntity<Integer> getTotalLogCount(
            @PathVariable Long userId,
            @PathVariable String logType
    ) {
        return ResponseEntity.ok(logService.getTotalLogCountByType(userId, logType));
    }
}
