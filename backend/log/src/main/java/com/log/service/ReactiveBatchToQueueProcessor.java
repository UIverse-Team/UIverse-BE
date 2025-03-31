package com.log.service;

import com.log.dto.ReviewRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

@Service
public class ReactiveBatchToQueueProcessor {

    private final BlockingQueue<List<ReviewRequest>> queue = new LinkedBlockingQueue<>();
    private final Sinks.Many<ReviewRequest> logSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Flux<ReviewRequest> logFlux = logSink.asFlux();
    private final LogService logService;

    private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public ReactiveBatchToQueueProcessor(LogService logService) {
        this.logService = logService;
    }

    // 로그 추가 메서드
    public void addLog(ReviewRequest request) {
        logSink.emitNext(request, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @PostConstruct
    public void startBatchProcessor() {
        logFlux.bufferTimeout(100, Duration.ofSeconds(1)) // 1초 또는 10개 단위로 버퍼링
                .filter(batch -> !batch.isEmpty()) // 빈 배치는 필터링
                .subscribe(batch -> {
                    singleThreadExecutor.execute(() -> { // 싱글 스레드로 BlockingQueue에 삽입
                        try {
                            queue.put(batch); // BlockingQueue에 배치 데이터 삽입
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                }, error -> System.err.println("Error processing batch: " + error));

        // 싱글 스레드 작업자 (예시)
        new Thread(() -> {
            while (true) {
                try {
                    List<ReviewRequest> batch = queue.take(); // BlockingQueue에서 데이터 꺼내기
                    saveBatch(batch); // 배치 저장
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    // 배치 저장 로직
    private void saveBatch(List<ReviewRequest> batch) {
        logService.saveAll(batch);
    }
}
