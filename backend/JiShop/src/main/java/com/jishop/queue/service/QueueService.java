package com.jishop.queue.service;

import com.jishop.queue.domain.Task;

import java.util.List;

public interface QueueService {

    String enqueueTask(Task task);
    Task dequeueTask();
    void completeTask(Task task);
    void failTask(Task task);
    Long getQueueSize();
    List<Task> getDeadLetterTasks();
    Task getTaskById(String taskId);
    Task requeueDeadLetter(String taskId);
    void incrementActiveRequests();
    void decrementActiveRequests();
    int getCurrentActiveRequests();
    boolean useQueue();
}
