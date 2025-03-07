package com.example.log;

import com.example.log.domain.enums.UserActionType;
import com.example.log.dto.LogRequest;
import com.example.log.service.LogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class LogServiceTest {

    @Autowired
    private LogService logService;

    @Test
    @DisplayName("로그 저장")
    void addLog() throws Exception {
        // given
        int threadCount = 10; // 동시 실행할 쓰레드 수
        var executor = Executors.newFixedThreadPool(threadCount);
        List<LogRequest> list = new ArrayList<>();

        for (Long id = 1L; id < 100000; id++) {
            list.add(new LogRequest(id, "test" + id, UserActionType.BUTTON_CLICK));
        }
        //when
        long s = System.currentTimeMillis();
        for (LogRequest logRequest : list) {
            executor.submit(() -> logService.addLog(logRequest));
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
        long e = System.currentTimeMillis();
        //then
        System.out.println("총 실행 시간 = " + (e - s) / 1000 + "초");
    }
}
