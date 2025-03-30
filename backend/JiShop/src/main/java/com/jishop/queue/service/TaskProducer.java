package com.jishop.queue.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface TaskProducer {

    CompletableFuture<String> submitTask(String type, Map<String, Object> payload, int priority);
}
