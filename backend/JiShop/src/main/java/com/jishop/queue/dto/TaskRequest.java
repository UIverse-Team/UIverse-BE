package com.jishop.queue.dto;

import java.util.Map;

public record TaskRequest(
        String type,
        Map<String, Object> payload,
        int priority
) {
}
