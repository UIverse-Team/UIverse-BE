package com.jishop.queue.controller;

import com.jishop.queue.domain.Task;
import com.jishop.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dead-letter")
public class DeadLetterQueueImpl implements DeadLetterQueue {

    private final QueueService queueService;

    // 실패 작업 조회
    @GetMapping()
    public ResponseEntity<?> getDeadLetterTasks() {
        return ResponseEntity.ok(queueService.getDeadLetterTasks());
    }

    // 실패 작업 재시도
    @PostMapping("/{taskId}/retry")
    public ResponseEntity<?> retryDeadLetterTasks(@PathVariable String taskId) {
        Task retried = queueService.requeueDeadLetter(taskId);
        if(retried == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message",
                    "Dead Letter Task not found"));
        }
        return ResponseEntity.accepted().body(Map.of("taskId", retried.getId(),
                "status", "requeued"));
    }
}