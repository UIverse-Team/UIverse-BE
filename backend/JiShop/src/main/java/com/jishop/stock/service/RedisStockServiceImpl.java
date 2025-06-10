package com.jishop.stock.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.stock.domain.Stock;
import com.jishop.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
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
    private static final String STOCK_GLOBAL_LOCK = "global:stock:lock";
    private static final int CACHE_TTL_HOURS = 72;

    // Redis에서 재고 확인, 없으면 DB에서 조회하여 캐싱
    @Override
    public boolean checkStock(Long saleProductId, int quantity) {
        Integer stock = getStockFromCache(saleProductId);
        return stock != null && stock >= quantity;
    }

    @Override
    public boolean checkMultipleStocks(Map<Long, Integer> productQuantityMap) {
        return productQuantityMap.entrySet().stream()
                .allMatch(entry -> checkStock(entry.getKey(), entry.getValue()));
    }

    @Override
    public boolean decreaseStock(Long saleProductId, int quantity) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        RAtomicLong atomicStock = redisson.getAtomicLong(key);

        //음수 방지를 위한 처리
        long currentStock = atomicStock.get();
        if(currentStock < quantity)
            return false;

        long newValue = atomicStock.addAndGet(-quantity);

        //실제로 재고가 부족해진 경우
        if(newValue < 0){
            //롤백
            atomicStock.addAndGet(quantity);
            return false;
        }

        //TTL 연장
        redisson.getKeys().expire(key, CACHE_TTL_HOURS, TimeUnit.HOURS);
        return true;
    }

    @Override
    public boolean decreaseMultipleStocks(Map<Long, Integer> productQuantityMap) {
        //글로벌 락 사용 => 여러 상품 재고 원자적으로 감소
        RLock lock = redisson.getLock(STOCK_GLOBAL_LOCK);

        try{
            //짧은 대기 시간으로 락 획득 시도
            if(!lock.tryLock(3,5,TimeUnit.SECONDS))
                return false;

            //모든 상품의 재고 다시 확인
            if(!checkMultipleStocks(productQuantityMap))
                return false;

            //모든 상품의 재고 감소 시도
            Map<Long, Boolean> decreaseResults = redisson.getMap("temp:decrease:results");
            decreaseResults.clear();

            for(Map.Entry<Long, Integer> entry : productQuantityMap.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();

                boolean success = decreaseStock(productId, quantity);
                decreaseResults.put(productId, success);

                //하나라도 실패하면 롤백
                if(!success){
                    //성공했던 상품들도 롤백
                    for(Map.Entry<Long, Boolean> result : decreaseResults.entrySet()){
                        if(Boolean.TRUE.equals(result.getValue())){
                            //이미 성공한 감소 롤백
                            String key = STOCK_KEY_PREFIX + result.getKey();
                            RAtomicLong atomicStock = redisson.getAtomicLong(key);
                            atomicStock.addAndGet(productQuantityMap.get(result.getKey()));
                        }
                    }
                    return false;
                }
            }
            decreaseResults.clear();
            return true;
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
            log.error("재고 감소 처리 중 인터럽트 발생", e);
            return false;
        } finally {
            if(lock.isHeldByCurrentThread())
                lock.unlock();
        }
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

            conditionalSyncCacheWithDb(saleProductId, stock.getQuantity());

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
            conditionalSyncCacheWithDb(saleProductId, stock.getQuantity());

            log.debug("재고 증가 동기화 완료: 상품 ID {}, 증가량 {}, 최종 수량 {}",
                    saleProductId, quantity, stock.getQuantity());
        } catch (Exception e) {
            log.error("재고 증가 동기화 실패: {}", e.getMessage(), e);
        }
    }

    public Integer getStockFromCache(Long saleProductId) {
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

    private void conditionalSyncCacheWithDb(Long saleProductId, int dbquantity) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        RAtomicLong atomicStock = redisson.getAtomicLong(key);

        RLock lock = redisson.getLock(key + ":sync");

        try{
            if(lock.tryLock(1,3, TimeUnit.SECONDS)) {
                try {
                    //캐시된 값이 DB보다 작으면 DB 값으로 업데이트
                    long cachedValue = atomicStock.get();

                    if (cachedValue < 0 || cachedValue > dbquantity) {
                        atomicStock.set(dbquantity);
                        redisson.getKeys().expire(key, CACHE_TTL_HOURS, TimeUnit.HOURS);
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("재고 캐시 동기화 중 인터럽트 발생", e);
        }
    }
}