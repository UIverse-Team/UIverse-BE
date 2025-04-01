package com.jishop.log;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LogRepositoryTest {
    @Autowired
    private LogRepository logRepository;

    @Test
    public void testFindRecentPageViewsByUserId() {
        Long testUserId = 1L; // 실제 존재하는 사용자 ID로 대체

        var pageViews = logRepository.findRecentPageViewsByUserId(testUserId);

        pageViews.forEach(System.out::println);
        assertThat(pageViews).isNotNull();
        assertThat(pageViews.size()).isLessThanOrEqualTo(10);
    }

    @Test
    public void testCountTotalLogsByType() {
        Long testUserId = 1L; // 실제 존재하는 사용자 ID로 대체

        int pageViewCount = logRepository.countTotalLogsByType(testUserId, "pageView");

        assertThat(pageViewCount).isGreaterThanOrEqualTo(0);
    }
}