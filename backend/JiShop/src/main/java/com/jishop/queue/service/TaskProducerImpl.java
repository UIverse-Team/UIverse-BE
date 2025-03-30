package com.jishop.queue.service;

import com.jishop.queue.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TaskProducerImpl implements TaskProducer {

    private final QueueService queueService;

    // 생산자 -> 외부 작업 요청 들어올시 Task 객체 만들고 Redis 큐에 등록
    @Async
    public CompletableFuture<String> submitTask(String type, Map<String, Object> payload, int priority){
        Task task = Task.of(type, payload, priority);
        String taskId = queueService.enqueueTask(task);
        return CompletableFuture.completedFuture(taskId);
    }
}
