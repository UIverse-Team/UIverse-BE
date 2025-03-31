package com.jishop.queue.service;

import com.jishop.queue.domain.TaskType;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface TaskProducer {

    CompletableFuture<String> submitTask(TaskType type, Map<String, Object> payload, int priority);
}