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

    public <T> T executeWithLock(String lockName, Supplier<T> supplier){
        return executeWithLock(lockName, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, DEFAULT_RETRY_COUNT, supplier);
    }

    public <T> T executeWithMultipleLocks(List<String> lockNames, Supplier<T> supplier){
        return executeWithMultipleLocks(lockNames, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, DEFAULT_RETRY_COUNT, supplier);
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
                    Thread.sleep(100 * (long)Math.pow(2, attempts));
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

    // 여러 락을 필요한 순서대로 획득
    public <T> T executeWithMultipleLocks(List<String> lockNames, long waitTime, long leaseTime, int retryCount, Supplier<T> supplier) {
        List<String> sortedLockNames = lockNames.stream().sorted().toList();
        List<RLock> locks = sortedLockNames.stream()
                .map(redisson::getLock)
                .toList();

        int attempts = 0;

        while (attempts < retryCount) {
            boolean allLocked = true;

            try {
                for (RLock lock : locks) {
                    boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
                    if (!acquired) {
                        allLocked = false;
                        log.warn("락 획득 실패 ({}/{}): {}", attempts + 1, retryCount, lock.getName());
                        break;
                    }
                }

                if (allLocked) {
                    return supplier.get();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("락 처리 중단됨", e);
                throw new DomainException(ErrorType.CONCURRENT_ORDER_PROCESSING);
            } catch (Exception e) {
                log.error("락 처리 중 예외 발생: {}", e.getMessage(), e);
                throw e;
            } finally {
                // 시도 실패 또는 성공 모두 락 해제
                for (RLock lock : locks) {
                    try {
                        if (lock.isHeldByCurrentThread()) {
                            lock.unlock();
                            log.debug("락 해제: {}", lock.getName());
                        }
                    } catch (Exception e) {
                        log.error("락 해제 중 에러: {}", e.getMessage());
                    }
                }
            }

            attempts++;
            try {
                Thread.sleep(100 * (long)Math.pow(2, attempts));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DomainException(ErrorType.CONCURRENT_ORDER_PROCESSING);
            }
        }

        throw new DomainException(ErrorType.LOCK_ACQUISITION_FAILED);
    }

}