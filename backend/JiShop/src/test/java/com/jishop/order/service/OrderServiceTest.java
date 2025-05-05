package com.jishop.order.service;

import com.jishop.address.dto.AddressRequest;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    @DisplayName("주문 30개 동시에 넣기")
    void 주문_30개_동시에_넣기() throws InterruptedException {
        // 테스트를 위한 트랜잭션 없이 재고 설정
        setupInitialStock(1000L, 200);

        // 스레드 안전한 컬렉션 사용
        List<OrderResponse> orderResponses = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch startLatch = new CountDownLatch(1); // 모든 스레드가 동시에 시작하도록 설정
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    // 모든 스레드가 준비될 때까지 대기
                    startLatch.await();

                    OrderRequest orderRequest = createSampleOrderRequest();
                    // 실제 orderService 메서드 호출하여 주문 생성
                    OrderResponse response = orderService.createOrder(null, orderRequest);
                    orderResponses.add(response);
                } catch (Exception e) {
                    e.printStackTrace();
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

        assertTrue(completed, "모든 스레드가 시간 내에 완료되어야 합니다.");
        assertEquals(0, failCount.get(), "모든 주문이 성공해야 합니다.");
        assertEquals(THREAD_COUNT, orderResponses.size(), "모든 주문이 성공적으로 생성되어야 합니다.");

        // 주문 번호의 유일성 검증
        long uniqueOrderNumbers = orderResponses.stream()
                .map(OrderResponse::orderNumber)
                .distinct()
                .count();
        assertEquals(THREAD_COUNT, uniqueOrderNumbers, "모든 주문 번호는 유일해야 합니다.");

        // 재고 감소 확인 - DB와 Redis 모두 확인
        Stock finalStock = stockRepository.findBySaleProduct_Id(1000L).orElseThrow();

        assertEquals(200 - THREAD_COUNT, finalStock.getQuantity(), "DB 재고가 정확히 감소해야 합니다.");
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
        assertEquals(quantity, redisStockService.checkStock(productId, stock.getQuantity()));
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

    @Test
    @DisplayName("재고가 29개인 상품을 10개의 스레드가 3개씩 동시에 구매했을 때 하나의 구매가 실패한다")
    void 테스트_재고() throws InterruptedException {
        // 실패 카운트
        AtomicInteger failCount = new AtomicInteger(0);

        // 테스트 전에 재고를 정확히 29개로 설정
        setupInitialStock(1000L, 29);

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await(); // 모든 스레드가 동시에 시작하도록 대기

                    // 주문 생성을 위한 요청 객체 생성
                    OrderRequest orderRequest = createSampleOrderRequestWithQuantity(3); // 각 주문당 3개 구매

                    try {
                        orderService.createOrder(null, orderRequest); // null은 비회원 주문을 의미
                    } catch (Exception e) {
                        // 재고 부족 등의 예외 발생 시 실패 카운트 증가
                        failCount.incrementAndGet();
                        System.out.println("주문 실패: " + e.getMessage());
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println("예외 발생: " + e.getMessage());
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 모든 스레드 시작 신호
        startLatch.countDown();

        // 모든 스레드 완료 대기
        endLatch.await(30, TimeUnit.SECONDS);
        executorService.shutdown();

        // 비동기 작업이 완료될 시간을 주기 위해 잠시 대기
        Thread.sleep(3000);

        // Then
        Stock updatedStock = stockRepository.findBySaleProduct_Id(1000L).orElseThrow();

        System.out.println("남은 DB 재고: " + updatedStock.getQuantity());

        // 검증: 하나의 주문이 실패해야 함
        assertEquals(1, failCount.get(), "하나의 주문이 실패해야 합니다");

        // 검증: 재고는 2개가 남아야 함 (29개 - (9개 스레드 * 3개 구매) = 2개)
        assertEquals(2, updatedStock.getQuantity(), "남은 DB 재고는 2개여야 합니다");
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