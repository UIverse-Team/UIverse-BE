package com.jishop.order.service;

import com.jishop.address.dto.AddressRequest;
import com.jishop.common.exception.DomainException;
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.stock.domain.Stock;
import com.jishop.stock.repository.StockRepository;
import com.jishop.stock.service.RedisStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Execution(ExecutionMode.SAME_THREAD) // 테스트 간 격리를 위해 변경
public class OrderServiceTest {

    @Autowired
    private OrderCreationService orderService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RedisStockService redisStockService;

    private static final int THREAD_COUNT = 100;
    private ExecutorService executorService;
    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        latch = new CountDownLatch(THREAD_COUNT);
    }

    @Test
    @DisplayName("주문 100개 동시에 넣기 - 개선된 테스트")
    void 개선된_동시성_테스트() throws InterruptedException {
        // 테스트를 위한 트랜잭션 없이 재고 설정
        final int INITIAL_STOCK = 200;
        final Long PRODUCT_ID = 1000L;
        setupInitialStock(PRODUCT_ID, INITIAL_STOCK);

        // 스레드 안전한 컬렉션 사용
        List<OrderResponse> orderResponses = Collections.synchronizedList(new ArrayList<>());

        // 오류 세부 정보 수집
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        CountDownLatch startLatch = new CountDownLatch(1); // 모든 스레드가 동시에 시작하도록 설정
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 랜덤 지연 생성기 - 더 극단적인 경쟁 조건 생성
        Random random = new Random();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadNumber = i;
            executorService.submit(() -> {
                try {
                    // 모든 스레드가 준비될 때까지 대기
                    startLatch.await();

                    // 더 무작위적인 실행 패턴을 위해 약간의 지연 적용 (0-50ms)
                    if (threadNumber % 3 == 0) { // 1/3의 스레드만 지연
                        Thread.sleep(random.nextInt(50));
                    }

                    OrderRequest orderRequest = createSampleOrderRequest();
                    // 실제 orderService 메서드 호출하여 주문 생성
                    OrderResponse response = orderService.createOrder(null, orderRequest);
                    orderResponses.add(response);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    exceptions.add(e);
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드 동시에 시작
        startLatch.countDown();

        // 모든 스레드 완료 대기
        boolean completed = latch.await(60, TimeUnit.SECONDS); // 더 긴 타임아웃 설정
        executorService.shutdown();

        // 비동기 작업이 완료될 시간을 주기 위해 잠시 대기
        Thread.sleep(2000);

        // 예외 요약 출력
        if (!exceptions.isEmpty()) {
            Map<String, Integer> exceptionCounts = new HashMap<>();
            for (Exception e : exceptions) {
                String key = e.getClass().getSimpleName() + ": " + e.getMessage();
                exceptionCounts.put(key, exceptionCounts.getOrDefault(key, 0) + 1);
            }
            System.out.println("발생한 예외 요약:");
            exceptionCounts.forEach((key, count) -> System.out.println(key + " - " + count + "회 발생"));
        }

        assertTrue(completed, "모든 스레드가 시간 내에 완료되어야 합니다.");
        assertEquals(0, failCount.get(), "모든 주문이 성공해야 합니다.");
        assertEquals(THREAD_COUNT, successCount.get(), "모든 주문이 성공적으로 생성되어야 합니다.");
        assertEquals(THREAD_COUNT, orderResponses.size(), "모든 주문이 응답을 반환해야 합니다.");

        // 주문 번호의 유일성 검증
        long uniqueOrderNumbers = orderResponses.stream()
                .map(OrderResponse::orderNumber)
                .distinct()
                .count();
        assertEquals(THREAD_COUNT, uniqueOrderNumbers, "모든 주문 번호는 유일해야 합니다.");

        // 재고 감소 확인 - DB 확인
        Stock finalStock = stockRepository.findBySaleProduct_Id(PRODUCT_ID).orElseThrow();
        assertEquals(INITIAL_STOCK - THREAD_COUNT, finalStock.getQuantity(),
                "DB 재고가 정확히 감소해야 합니다.");

        // Redis 재고 확인 - Redis와 DB의 동기화 검증
        int redisStock = redisStockService.getStockFromCache(PRODUCT_ID);
        assertEquals(finalStock.getQuantity(), redisStock,
                "Redis 재고가 DB와 동일해야 합니다.");
    }


    @Test
    @DisplayName("재고 부족 상황에서 동시 주문 처리")
    void 재고_부족_상황_동시_주문_테스트() throws InterruptedException {
        // 재고를 의도적으로 주문 수보다 적게 설정 (50개)
        final int INITIAL_STOCK = 50;
        final Long PRODUCT_ID = 1000L;
        setupInitialStock(PRODUCT_ID, INITIAL_STOCK);

        List<OrderResponse> orderResponses = Collections.synchronizedList(new ArrayList<>());
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    OrderRequest orderRequest = createSampleOrderRequest();
                    OrderResponse response = orderService.createOrder(null, orderRequest);
                    orderResponses.add(response);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    exceptions.add(e);
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        startLatch.countDown();
        latch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();
        Thread.sleep(2000);

        // 재고만큼만 주문이 성공해야 함
        assertEquals(INITIAL_STOCK, successCount.get(),
                "재고만큼만 주문이 성공해야 합니다.");
        assertEquals(THREAD_COUNT - INITIAL_STOCK, failCount.get(),
                "나머지 주문은 실패해야 합니다.");

        // 재고가 정확히 0이 되었는지 확인
        Stock finalStock = stockRepository.findBySaleProduct_Id(PRODUCT_ID).orElseThrow();
        assertEquals(0, finalStock.getQuantity(), "DB 재고가 0이어야 합니다.");

        // Redis 재고도 0인지 확인
        int redisStock = redisStockService.getStockFromCache(PRODUCT_ID);
        assertEquals(0, redisStock, "Redis 재고도 0이어야 합니다.");

        // 예외 유형 확인 - 대부분 재고 부족 예외여야 함
        long outOfStockExceptions = exceptions.stream()
                .filter(e -> e instanceof DomainException ||
                        e.getMessage().contains("재고") ||
                        e.getMessage().contains("stock"))
                .count();
        assertTrue(outOfStockExceptions > 0,
                "재고 부족 관련 예외가 발생해야 합니다.");
    }

    // DB와 Redis 모두 재고를 설정하는 메서드
    private void setupInitialStock(Long productId, int quantity) {
        Stock stock = stockRepository.findBySaleProduct_Id(productId).orElseThrow();
        int currentQuantity = stock.getQuantity();

        // DB 재고 설정
        if (currentQuantity != quantity) {
            if (currentQuantity < quantity) {
                stock.increaseStock(quantity - currentQuantity);
            } else {
                stock.decreaseStock(currentQuantity - quantity);
            }
            stockRepository.saveAndFlush(stock);
        }

        // Redis 캐시 강제 업데이트
        redisStockService.syncCacheWithDb(productId, quantity);

        // 확인
        assertEquals(quantity, stock.getQuantity());
        assertTrue(redisStockService.checkStock(productId, stock.getQuantity()));
    }

    private OrderRequest createSampleOrderRequest() {
        // 주소 정보 생성
        AddressRequest addressRequest = new AddressRequest(
                "홍길동",
                "010-1234-5678",
                "12345",
                "서울특별시 강남구 테헤란로 123",
                "456동 789호",
                false
        );

        // 주문 상품 목록 생성 - 각 주문당 구매량을 1개로 설정
        List<OrderDetailRequest> orderDetailRequestList = List.of(
                new OrderDetailRequest(1000L, 1)
        );

        // OrderRequest 객체 생성 및 반환
        return new OrderRequest(addressRequest, orderDetailRequestList);
    }


    private OrderRequest createSampleOrderRequestWithQuantity(int quantity) {
        // 주소 정보 생성
        AddressRequest addressRequest = new AddressRequest(
                "홍길동",
                "010-1234-5678",
                "12345",
                "서울특별시 강남구 테헤란로 123",
                "456동 789호",
                false
        );

        // 주문 상품 목록 생성 - 지정된 수량으로 설정
        List<OrderDetailRequest> orderDetailRequestList = List.of(
                new OrderDetailRequest(1000L, quantity)
        );

        // OrderRequest 객체 생성 및 반환
        return new OrderRequest(addressRequest, orderDetailRequestList);
    }
}