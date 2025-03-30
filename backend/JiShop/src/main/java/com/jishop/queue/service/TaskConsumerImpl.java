package com.jishop.queue.service;

import com.jishop.queue.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskConsumerImpl implements TaskConsumer {

    private final QueueService queueService;

    // 작업 유형별 실제 처리
    private void processTaskByType(Task task) {
        switch (task.getType()) {
            case "EMAIL":
                break;
            case "SOCIAL_LOGIN":
                break;
            default:
                throw new UnsupportedOperationException("지원하지 않는 작업 유형: " + task.getType());
        }
    }

    // 다음 작업 하나를 처리
    public void processNextTask() {
        Task task = queueService.dequeueTask();

        if(task == null) return;

        try {
            log.info("작업 처리 중: {}", task.getId());
            processTaskByType(task);
            queueService.completeTask(task);
            log.info("작업 완료: {}", task.getId());
        } catch (Exception e) {
            log.error("작업 실패: " + task.getId(), e);
            queueService.failTask(task);
        }
    }
}
