package com.jishop.order.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private final RedissonClient redisson;
    private static final long DEFAULT_WAIT_TIME = 10L; //기다리는 시간 증가
    private static final long DEFAULT_LEASE_TIME = 15L; // 락 유지 시간 증가
    private static final int DEFAULT_RETRY_COUNT = 3;

    public <T> T executeWithLock(String lockName, Supplier<T> supplier) {
        return executeWithLock(lockName, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, DEFAULT_RETRY_COUNT, supplier);
    }

    public <T> T executeWithLock(String lockName, long waitTime, long leaseTime, int retryCount, Supplier<T> supplier) {
        RLock lock = redisson.getLock(lockName);
        boolean isLocked = false;
        int attempts = 0;

        while (attempts < retryCount) {
            try {
                log.debug("락 얻기 시도 ({}/{}): {}", attempts + 1, retryCount, lockName);
                isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

                if (!isLocked) {
                    log.warn("락 얻기 실패 ({}/{}): {}", attempts + 1, retryCount, lockName);
                    attempts++;
                    //지수 백오프 적용
                    Thread.sleep(100 * (long) Math.pow(2, attempts));
                    continue;
                }

                log.debug("락 얻기 성공: {}", lockName);
                return supplier.get();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("락 방해됨: {}", lockName, e);
                throw new DomainException(ErrorType.CONCURRENT_ORDER_PROCESSING);
            } catch (Exception e) {
                log.error("락 처리 중 에러 발생 {}: {}", lockName, e.getMessage(), e);
                throw e;
            } finally {
                if (isLocked && lock.isHeldByCurrentThread()) {
                    try {
                        lock.unlock();
                        log.debug("락 해제: {}", lockName);
                    } catch (Exception e) {
                        log.error("락 해제 중 에러 발생 {}: {}", lockName, e.getMessage(), e);
                    }
                }
            }
        }
        // 모든 재시도 후에도 실패한다면
        throw new DomainException(ErrorType.LOCK_ACQUISITION_FAILED);
    }
}