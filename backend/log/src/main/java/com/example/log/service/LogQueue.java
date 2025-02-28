package com.example.log.service;

import com.example.log.domain.Log;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class LogQueue {

    private final BlockingQueue<Log> queue = new LinkedBlockingQueue<>();

    public void addLog(Log log) {
        queue.offer(log);
    }

    public Log takeLog() throws InterruptedException {
        return queue.take();
    }

    public int getQueueSize() {
        return queue.size();
    }

    public Log pollLog() {
        return queue.poll();
    }
}
