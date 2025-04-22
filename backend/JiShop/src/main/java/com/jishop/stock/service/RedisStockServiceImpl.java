package com.jishop.stock.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.stock.domain.Stock;
import com.jishop.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisStockServiceImpl implements RedisStockService {

    private final RedissonClient redisson;
    private final StockRepository stockRepository;
    private static final String STOCK_KEY_PREFIX = "stock:";
    private static final int CACHE_TTL_HOURS = 24;

    // 개별 상품 캐시 TTL 설정(상품ID에 따라 다른 TTL 적용 가능)
    private final Map<Long, Integer> productTTLMap = new HashMap<>();

    public Map<Long, Boolean> batchCheckStock(Map<Long, Integer> productQuantityMap) {
        Map<Long, Boolean> results = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : productQuantityMap.entrySet()) {
            results.put(entry.getKey(), checkStock(entry.getKey(), entry.getValue()));
        }

        return results;
    }

    // 여러 상품의 재고 한번에 감소
    public Map<Long, Boolean> batchDecreaseStock(Map<Long, Integer> productQuantityMap) {
        Map<Long, Boolean> results = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : productQuantityMap.entrySet()) {
            results.put(entry.getKey(), decreaseStock(entry.getKey(), entry.getValue()));
        }

        return results;
    }

    // Redis에서 재고 확인, 없으면 DB에서 조회하여 캐싱
    @Override
    public boolean checkStock(Long saleProductId, int quantity) {
        Integer stock = getStockFromCache(saleProductId);
        return stock != null && stock >= quantity;
    }

    // Redisson을 사용한 재고 감소 처리
    @Override
    public boolean decreaseStock(Long saleProductId, int quantity) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        RAtomicLong atomicStock = redisson.getAtomicLong(key);
        String lockKey = "lock:" + key;

        var lock = redisson.getLock(lockKey);
        boolean locked = false;

        try {
            // 0.5초 대기 후 1초 동안 락 소유
            locked = lock.tryLock(500, 1000, TimeUnit.MILLISECONDS);
            if (!locked) {
                return false;
            }

            long currentStock = atomicStock.get();
            if (currentStock < quantity) {
                return false;
            }

            atomicStock.addAndGet(-quantity);
            int ttl = productTTLMap.getOrDefault(saleProductId, CACHE_TTL_HOURS);
            redisson.getKeys().expire(key, ttl, TimeUnit.HOURS);

            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 인터럽트 상태 복구
            log.error("재고 감소 중 인터럽트 발생: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Redis stock decrease operation failed: {}", e.getMessage());
            return false;
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    // Redis 캐시와 DB를 동기화하여 재고 감소 처리
    @Override
    @Async("stockTaskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void syncStockDecrease(Long saleProductId, int quantity) {
        try {
            Stock stock = stockRepository.findBySaleProduct_IdWithPessimisticLock(saleProductId)
                    .orElseThrow(() -> new DomainException(ErrorType.STOCK_NOT_FOUND));

            stock.decreaseStock(quantity);
            stockRepository.saveAndFlush(stock);

            syncCacheWithDb(saleProductId, stock.getQuantity());

            log.debug("재고 동기화 완료: 상품 ID {}, 감소량 {}, 남은 수량 {}",
                    saleProductId, quantity, stock.getQuantity());
        } catch (Exception e) {
            log.error("재고 동기화 실패: {}", e.getMessage(), e);
        }
    }

    @Override
    @Async("stockTaskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public void syncStockIncrease(Long saleProductId, int quantity) {
        try {
            Stock stock = stockRepository.findBySaleProduct_IdWithPessimisticLock(saleProductId)
                    .orElseThrow(() -> new DomainException(ErrorType.STOCK_NOT_FOUND));

            // DB 재고 증가
            stock.increaseStock(quantity);
            stockRepository.saveAndFlush(stock);

            // Redis 캐시 업데이트
            syncCacheWithDb(saleProductId, stock.getQuantity());

            log.debug("재고 증가 동기화 완료: 상품 ID {}, 증가량 {}, 최종 수량 {}",
                    saleProductId, quantity, stock.getQuantity());
        } catch (Exception e) {
            log.error("재고 증가 동기화 실패: {}", e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getStockFromCache(Long saleProductId) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        RAtomicLong atomicStock = redisson.getAtomicLong(key);
        long stockValue = atomicStock.get();

        // 값이 0이고 키가 존재하지 않는 경우 (기본값)
        if (stockValue == 0 && redisson.getKeys().countExists(key) == 0) {
            // 캐시에 없으면 DB에서 조회 후 캐싱
            Optional<Stock> stockOpt = stockRepository.findBySaleProduct_Id(saleProductId);

            if (stockOpt.isPresent()) {
                int stockQuantity = stockOpt.get().getQuantity();

                // 상품별 TTL 적용
                int ttl = productTTLMap.getOrDefault(saleProductId, CACHE_TTL_HOURS);
                atomicStock.set(stockQuantity);
                redisson.getKeys().expire(key, ttl, TimeUnit.HOURS);

                return stockQuantity;
            }
            return null;
        }
        return (int) stockValue;
    }

    @Override
    public void syncCacheWithDb(Long saleProductId, int quantity) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        RAtomicLong atomicStock = redisson.getAtomicLong(key);
        atomicStock.set(quantity);

        int ttl = productTTLMap.getOrDefault(saleProductId, CACHE_TTL_HOURS);
        redisson.getKeys().expire(key, ttl, TimeUnit.HOURS);
    }

    // 상품별 캐시 TTL 설정
    public void setProductCacheTtl(Long productId, int hours) {
        if (hours > 0) {
            productTTLMap.put(productId, hours);
        }
    }

    // 캐시 강제 갱신
    public void refreshStockCache(Long saleProductId) {
        stockRepository.findBySaleProduct_Id(saleProductId).ifPresent(stock -> {
            syncCacheWithDb(saleProductId, stock.getQuantity());
        });
    }
}