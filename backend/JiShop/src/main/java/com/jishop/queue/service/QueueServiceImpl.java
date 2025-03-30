package com.jishop.queue.service;

import com.jishop.queue.domain.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {

    private static final String TASK_QUEUE = "taskQueue";
    private static final String PROCESSING_SET = "taskProcessing";
    private static final String DEAD_LETTER_QUEUE = "taskDeadLetter";

    private final RedisTemplate<String, Object> redisTemplate;

    // 작업을 우선순위 큐에 추가
    public String enqueueTask(Task task) {
        try{
            redisTemplate.opsForZSet().add(TASK_QUEUE, task, task.getPriority());
            return task.getId();
        } catch (Exception e) {
            throw new RuntimeException("큐 작업 추가 실패",e);
        }
    }

    // 우선순위 가장 높은 작업 꺼내오기
    public Task dequeueTask(){
        Set<Object> tasks = redisTemplate.opsForZSet().reverseRange(TASK_QUEUE, 0, 0);
        if(tasks != null && !tasks.isEmpty()) {
            Task task = (Task) tasks.iterator().next();
            redisTemplate.opsForZSet().remove(TASK_QUEUE, task);
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
            redisTemplate.opsForZSet().add(PROCESSING_SET, task, task.getPriority());
        } else {
            task.markAsFailed();
            redisTemplate.opsForList().rightPush(DEAD_LETTER_QUEUE, task);
        }
    }

    // 현재 대기 중인 작업 수 check
    public Long getQueueSize(){
        return redisTemplate.opsForZSet().size(TASK_QUEUE);
    }

    // 재시도 했지만 실패시 Dead 큐에 반환
    public List<Task> getDeadLetterTasks(){
        return redisTemplate.opsForList().range(DEAD_LETTER_QUEUE, 0, -1)
                .stream().map(obj -> (Task) obj).toList();
    }

    public Task getTaskById(String taskId){
        Set<Object> allTasks = redisTemplate.opsForSet().members(PROCESSING_SET);
        if(allTasks != null) {
            for(Object obj : allTasks) {
                Task task = (Task) obj;
                if(task.getId().equals(taskId)) return task;
            }
        }
        return null;
    }

    public Task requeueDeadLetter(String taskId){
        List<Object> deadTasks = redisTemplate.opsForList().range(DEAD_LETTER_QUEUE, 0, -1);
        for(Object obj : deadTasks) {
            Task task = (Task) obj;
            if(task.getId().equals(taskId)) {
                redisTemplate.opsForList().remove(DEAD_LETTER_QUEUE, 1, task);
                task.markAsRetry();
                enqueueTask(task);
                return task;
            }
        }
        return null;
    }
}