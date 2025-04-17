package com.jishop.queue.service;

import com.jishop.queue.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private static final String TASK_QUEUE = "taskQueue";
    private static final String PROCESSING_SET = "taskProcessing";
    private static final String DEAD_LETTER_QUEUE = "taskDeadLetter";

    private final RedisTemplate<String, Object> redisTemplate;

    // 동시 접속자수 카운트 모니터링을 위한 추가
    private final AtomicInteger activeRequests = new AtomicInteger(0);


    // 작업을 우선순위 큐에 추가
    public String enqueueTask(Task task) {
        try {
            redisTemplate.opsForList().rightPush(TASK_QUEUE, task); // 맨 뒤 삽입
            return task.getId();
        } catch (Exception e) {
            throw new RuntimeException("큐 추가 실패!", e);
        }
    }

    // 우선순위 가장 높은 작업 꺼내오기
    public Task dequeueTask(){
        Object obj = redisTemplate.opsForList().leftPop(TASK_QUEUE);
        if (obj != null) {
            Task task = (Task) obj;
            redisTemplate.opsForSet().add(PROCESSING_SET, task);
            return task;
        }

        return null;
    }

    // 작업이 완료
    public void completeTask(Task task){
        redisTemplate.opsForSet().remove(PROCESSING_SET, task);
        task.markAsDone();
    }

    // 작업 실패
    public void failTask(Task task){
        redisTemplate.opsForSet().remove(PROCESSING_SET, task);
        if(task.getRetryCount()<3) {
            task.markAsRetry();
            redisTemplate.opsForList().rightPush(TASK_QUEUE, task);
        } else {
            task.markAsFailed();
            redisTemplate.opsForList().rightPush(DEAD_LETTER_QUEUE, task);
        }
    }

    // 현재 대기 중인 작업 수 check
    public Long getQueueSize(){
        return redisTemplate.opsForList().size(TASK_QUEUE);
    }


    public Task getTaskById(String taskId){
        // 1. 처리 중 작업 조회
        Set<Object> processingTasks = redisTemplate.opsForSet().members(PROCESSING_SET);
        if (processingTasks != null) {
            for (Object obj : processingTasks) {
                Task task = (Task) obj;
                if (task.getId().equals(taskId)) return task;
            }
        }

        // 2. 대기 중 작업 조회 (List)
        List<Object> queuedTasks = redisTemplate.opsForList().range(TASK_QUEUE, 0, -1);
        if (queuedTasks != null) {
            for (Object obj : queuedTasks) {
                Task task = (Task) obj;
                if (task.getId().equals(taskId)) return task;
            }
        }

        return null;
    }

    // 동시 요청 수 증가 메서드
    public void incrementActiveRequests() {
        activeRequests.incrementAndGet();
    }

    // 동시 요청 수 감소 메서드
    public void decrementActiveRequests() {
        activeRequests.decrementAndGet();
    }

    // 현재 활성 요청 수 조회 메서드
    public int getCurrentActiveRequests() {
        return activeRequests.get();
    }

    // 부하에 따른 로직 결정
    public boolean useQueue(){
        return getCurrentActiveRequests() > 50;
    }
}