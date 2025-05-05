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

    // Redis에서 재고 확인, 없으면 DB에서 조회하여 캐싱
    @Override
    public boolean checkStock(Long saleProductId, int quantity) {
        Integer stock = getStockFromCache(saleProductId);
        return stock != null && stock >= quantity;
    }

    @Override
    public boolean decreaseStock(Long saleProductId, int quantity) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        RAtomicLong atomicStock = redisson.getAtomicLong(key);

        long newValue = atomicStock.addAndGet(-quantity);

        if(newValue < 0){
            atomicStock.addAndGet(quantity);
            return false;
        }

        redisson.getKeys().expire(key, CACHE_TTL_HOURS, TimeUnit.HOURS);
        return true;
    }

    // Redis 캐시와 DB를 동기화하여 재고 감소 처리
    @Override
    @Async("stockTaskExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
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

    private Integer getStockFromCache(Long saleProductId) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        RAtomicLong atomicStock = redisson.getAtomicLong(key);
        long stockValue = atomicStock.get();

        // 값이 0이고 키가 존재하지 않는 경우 (기본값)
        if (stockValue == 0 && redisson.getKeys().countExists(key) == 0) {
            // 캐시에 없으면 DB에서 조회 후 캐싱
           return stockRepository.findBySaleProduct_Id(saleProductId)
                   .map(stock -> {
                       int stockQuantity = stock.getQuantity();
                       atomicStock.set(stockQuantity);
                       redisson.getKeys().expire(key, CACHE_TTL_HOURS, TimeUnit.HOURS);
                       return stockQuantity;
                   })
                   .orElse(null);
        }
        return (int) stockValue;
    }

    @Override
    public void syncCacheWithDb(Long saleProductId, int quantity) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        RAtomicLong atomicStock = redisson.getAtomicLong(key);
        atomicStock.set(quantity);

        redisson.getKeys().expire(key, CACHE_TTL_HOURS, TimeUnit.HOURS);
    }
}