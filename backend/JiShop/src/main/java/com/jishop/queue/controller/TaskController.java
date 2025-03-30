package com.jishop.queue.controller;

import com.jishop.queue.dto.TaskRequest;
import org.springframework.http.ResponseEntity;

public interface TaskController {

    ResponseEntity<?> addTask(TaskRequest request);
    ResponseEntity<?> getTaskStatus(String taskId);
    ResponseEntity<?> getQueueStatus();
}
