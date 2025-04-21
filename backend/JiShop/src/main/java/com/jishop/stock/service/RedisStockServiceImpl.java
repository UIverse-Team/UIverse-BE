package com.jishop.stock.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.stock.domain.Stock;
import com.jishop.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisStockServiceImpl implements RedisStockService{

    private final StringRedisTemplate redisTemplate;
    private final StockRepository stockRepository;
    private static final String STOCK_KEY_PREFIX = "stock:";
    private static final int CACHE_TTL_HOURS = 24;

    //Redis에서 재고 확인, 없으면 DB에서 조회하여 캐싱
    @Override
    public boolean checkStock(Long saleProductId, int quantity) {
        Integer stock = getStockFromCache(saleProductId);

        return stock != null && stock >= quantity;
    }

    //Redis에서 재고 감소 처리
    @Override
    public boolean decreaseStock(Long saleProductId, int quantity) {
        String key = STOCK_KEY_PREFIX + saleProductId;

        //Redis에서 재고 확인 후 감소
        Integer currentStock = getStockFromCache(saleProductId);

        if (currentStock == null || currentStock < quantity)
            return false;

        //Redis에서 재고 감소
        Long newStock = redisTemplate.opsForValue().decrement(key, quantity);

        //재고가 부족하면 롤백
        if (newStock < 0) {
            redisTemplate.opsForValue().increment(key, quantity);

            return false;
        }

        return true;
    }

    //Redis 캐시와 DB를 동기화하여 재고 감소 처리
    @Override
    public void syncStockDecrease(Long saleProductId, int quantity) {
        Stock stock = stockRepository.findBySaleProduct_Id(saleProductId)
                .orElseThrow(() -> new DomainException(ErrorType.STOCK_NOT_FOUND));

        //DB 재고 감소
        stock.decreaseStock(quantity);
        stockRepository.save(stock);

        //Redis 캐시 업데이트
        syncCacheWithDb(saleProductId, stock.getQuantity());
    }

    //주문 취소 시 Redis 캐시와 DB를 동기화하여 재고 증가 처리
    @Override
    public void syncStockIncrease(Long saleProductId, int quantity) {
        Stock stock = stockRepository.findBySaleProduct_Id(saleProductId)
                .orElseThrow(() -> new DomainException(ErrorType.STOCK_NOT_FOUND));

        //DB 재고 증가
        stock.increaseStock(quantity);
        stockRepository.save(stock);

        //Redis 캐시 업데이트
        syncCacheWithDb(saleProductId, stock.getQuantity());
    }

    //캐시에서 재고 정보 조회, 없으면 DB에서 조회해 캐싱
    @Override
    public Integer getStockFromCache(Long saleProductId) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        String cachedStock = redisTemplate.opsForValue().get(key);

        if(cachedStock != null)
            return Integer.parseInt(cachedStock);

        //캐시에 없으면 DB에서 조회 후 개킹
        Optional<Stock> stockOpt = stockRepository.findBySaleProduct_Id(saleProductId);

        if(stockOpt.isPresent()){
            int stockQuantity = stockOpt.get().getQuantity();
            redisTemplate.opsForValue().set(key, String.valueOf(stockQuantity), CACHE_TTL_HOURS, TimeUnit.HOURS);

            return stockQuantity;
        }

        return null;
    }

    //Redis 캐시를 DB와 동기화
    @Override
    public void syncCacheWithDb(Long saleProductId, int quantity) {
        String key = STOCK_KEY_PREFIX + saleProductId;
        redisTemplate.opsForValue().set(key, String.valueOf(quantity), CACHE_TTL_HOURS, TimeUnit.HOURS);
    }
}
