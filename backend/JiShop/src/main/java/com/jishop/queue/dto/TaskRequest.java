package com.jishop.queue.dto;

import com.jishop.queue.domain.TaskType;

import java.util.Map;

public record TaskRequest(
        TaskType type,
        Map<String, Object> payload
) {
}