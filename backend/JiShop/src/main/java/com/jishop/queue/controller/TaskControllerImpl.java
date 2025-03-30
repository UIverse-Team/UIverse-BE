package com.jishop.queue.controller;

import com.jishop.queue.domain.Task;
import com.jishop.queue.dto.TaskRequest;
import com.jishop.queue.service.QueueService;
import com.jishop.queue.service.TaskProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pend")
public class TaskControllerImpl {

    private final TaskProducer taskProducer;
    private final QueueService queueService;

    // 작업 추가
    @PostMapping("/tasks")
    public ResponseEntity<?> addTask(@RequestBody TaskRequest request) {
        try{
            CompletableFuture<String> future = taskProducer.submitTask(
                    request.type(), request.payload(), request.priority());

            if(queueService.getQueueSize()> 1000) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .header("Retry-After", "30")
                        .body(Map.of("taskId", future.get(), "status", "queued",
                                "message", "잠시 후 다시 시도해주세요!"));
            }

            return ResponseEntity.accepted()
                    .body(Map.of("taskId", future.get(), "status", "queued"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // 작업 상태 조회
    @GetMapping("/tasks/{taskId}/status")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskId) {
        Task task = queueService.getTaskById(taskId);
        if(task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "해당 작업을 찾을 수 없습니다!"));
        }
        return ResponseEntity.ok(Map.of("taskId", taskId, "status", "in_progress"));
    }

    // 큐 상태 조회
    @GetMapping("/queue/status")
    public ResponseEntity<?> getQueueStatus() {
        return ResponseEntity.ok(Map.of("queueSize", queueService.getQueueSize(),
                "timestamp", LocalDateTime.now()));
    }
}
