package com.jishop.queue.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task implements Serializable {

    private String id;
    // 작업 유형
    private TaskType type;
    // 작업에 필요한 데이터
    private Map<String, Object> payload;
    // 작업 생성된 시간
    private LocalDateTime createdAt;
    // 작업 실패 시 재시도 횟수 카운트
    private int retryCount;
    // 작업 상태 (PENDING, RETRY, FAILED, DONE)
    private String status;

    private Task(TaskType type, Map<String, Object> payload) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
        this.retryCount = 0;
        this.status = "PENDING";
    }

    public void markAsRetry() {
        this.retryCount++;
        this.status = "RETRY";
    }

    public void markAsFailed() {
        this.status = "FAILED";
    }

    public void markAsDone() {
        this.status = "DONE";
    }

    public static Task of(TaskType type, Map<String, Object> payload) {
        return new Task(type, payload);
    }
}