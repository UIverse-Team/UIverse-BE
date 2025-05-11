package com.jishop.stock.service;

import com.jishop.stock.domain.Stock;
import com.jishop.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockInitializationService {

    private final StockRepository stockRepository;
    private final RedissonClient redisson;
    private static final String STOCK_KEY_PREFIX = "stock:";
    private static final int CACHE_TTL_HOURS = 24;

    //todo: 인기상품 100개만 먼저 캐싱 후, 나중에 필요할때마다 캐싱하기
    @EventListener(ApplicationReadyEvent.class)
    public void initializeStocks(){
        log.info("재고 정보 Redis 캐싱 시작");

        List<Stock> allStocks = stockRepository.findAll();

        for(Stock stock : allStocks){
            String key = STOCK_KEY_PREFIX + stock.getSaleProduct().getId();
            RAtomicLong atomicStock = redisson.getAtomicLong(key);
            atomicStock.set(stock.getQuantity());
            redisson.getKeys().expire(key, CACHE_TTL_HOURS, TimeUnit.HOURS);        }

        log.info("재고 정보 Redis 캐싱 완료: {} 개 상품", allStocks.size());

    }
}
