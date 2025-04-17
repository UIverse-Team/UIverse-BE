package com.jishop.queue.service;

import com.jishop.member.domain.User;
import com.jishop.member.dto.request.SignInFormRequest;
import com.jishop.member.service.AuthService;
import com.jishop.queue.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskConsumerImpl implements TaskConsumer {

    private final AuthService authService;
    private final QueueService queueService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 작업 유형별 실제 처리
    private void processTaskByType(Task task) {
        switch (task.getType()) {
            case LOGIN -> handleLogin(task);
            case PAYMENTS -> handlePayment(task);
            default -> throw new UnsupportedOperationException("지원하지 않는 작업 유형: " + task.getType());
        }
    }

    // 로그인 처리
    private void handleLogin(Task task) {
        String loginId = (String) task.getPayload().get("loginId");
        String password = (String) task.getPayload().get("password");
        String sessionId = (String) task.getPayload().get("sessionId");

        try {
            // AuthService에 새 메서드 추가 필요
            User user = authService.attemptLogin(new SignInFormRequest(loginId, password));

            // Redis에 직접 세션 데이터 저장
            String sessionKey = "spring:session:sessions:" + sessionId;
            redisTemplate.opsForHash().put(sessionKey, "sessionAttr:userId", user.getId());

            // 세션 만료 시간 설정
            redisTemplate.expire(sessionKey, 3600, TimeUnit.SECONDS);

            log.info("사용자 {} 로그인 성공", user.getId());
        } catch (Exception e) {
            log.error("로그인 실패: {}", e.getMessage(), e);
            throw new RuntimeException("로그인 처리 중 오류 발생", e);
        }
    }

    // todo: 결제 처리 추가 예정 (4/17)
    private void handlePayment(Task task) {
        // todo: 결제 로직 대기열 붙이기
        String email = (String) task.getPayload().get("email");
    }

    // 작업 하나를 처리
    @Scheduled(fixedRate = 1000)
    public void processNextTask() {
        Task task = queueService.dequeueTask();

        if(task == null) return;

        try {
            log.info("작업 처리 중: {}", task.getId());
            processTaskByType(task);
            queueService.completeTask(task);
            log.info("작업 완료: {}", task.getId());
        } catch (Exception e) {
            log.error("작업 실패: {}", task.getId(), e);
            queueService.failTask(task);
        }
    }
}