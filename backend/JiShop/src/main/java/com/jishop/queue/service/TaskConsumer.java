package com.jishop.queue.service;

import com.jishop.queue.domain.Task;

public interface TaskConsumer {

    void processNextTask();
}
