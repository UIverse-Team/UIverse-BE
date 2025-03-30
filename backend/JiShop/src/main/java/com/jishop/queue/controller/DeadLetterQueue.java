package com.jishop.queue.controller;

import org.springframework.http.ResponseEntity;

public interface DeadLetterQueue {

    ResponseEntity<?> getDeadLetterTasks();
    ResponseEntity<?> retryDeadLetterTasks(String taskId);
}
