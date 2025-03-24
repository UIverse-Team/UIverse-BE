package com.jishop.order.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private final RedissonClient redisson;
    private static final long DEFAULT_WAIT_TIME = 5L;
    private static final long DEFAULT_LEASE_TIME = 5L;

    public <T> T executeWithLock(String lockName, Supplier<T> supplier){
        return executeWithLock(lockName, DEFAULT_WAIT_TIME, DEFAULT_LEASE_TIME, supplier);
    }

    public <T> T executeWithLock(String lockName, long waitTime, long leaseTime, Supplier<T> supplier) {
        RLock lock = redisson.getLock(lockName);
        try {
            boolean isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new DomainException(ErrorType.LOCK_ACQUISITION_FAILED);
            }
            try {
                return supplier.get();
            } finally {
                lock.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DomainException(ErrorType.CONCURRENT_ORDER_PROCESSING);
        }
    }
}
