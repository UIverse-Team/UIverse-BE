package com.jishop.log;

import com.jishop.log.dto.PageViewLogDto;
import com.jishop.log.dto.ProductClickLogDto;
import com.jishop.log.dto.ReviewLogDto;
import com.jishop.log.dto.SearchLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;


    public List<PageViewLogDto> getUserRecentPageViews(Long userId) {
        return logRepository.findRecentPageViewsByUserId(userId);
    }

    public List<SearchLogDto> getUserRecentSearches(Long userId) {
        return logRepository.findRecentSearchesByUserId(userId);
    }

    public List<ProductClickLogDto> findRecentProductClickLogs(Long userId, int page, int size) {
        return logRepository.findRecentProductClickLogs(userId, page, size);
    }

    public List<ReviewLogDto> getUserRecentReviewViews(Long userId) {
        return logRepository.findRecentReviewViewsByUserId(userId);
    }

    public int getTotalLogCountByType(Long userId, String logType) {
        return logRepository.countTotalLogsByType(userId, logType);
    }
}
