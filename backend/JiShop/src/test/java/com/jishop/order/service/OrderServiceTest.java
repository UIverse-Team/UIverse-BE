package com.jishop.order.service;

import com.jishop.address.dto.AddressRequest;
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.dto.OrderResponse;
import com.jishop.stock.domain.Stock;
import com.jishop.stock.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Execution(ExecutionMode.CONCURRENT)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    private static final int THREAD_COUNT = 10;
    private ExecutorService executorService;
    private CountDownLatch latch;
    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        latch = new CountDownLatch(THREAD_COUNT);
    }

    @Test
    void 주문_10개_동시에_넣기() throws InterruptedException {
        Long saleProductId = 21L;

        // 재고를 29개로 설정
        Stock stock = stockRepository.findBySaleProduct_Id(saleProductId).orElseThrow();
        stock.increaseStock(29 - stock.getQuantity()); // 현재 재고를 29개로 맞춤
        stockRepository.save(stock);

        // 스레드 안전한 컬렉션 사용
        List<OrderResponse> orderResponses = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch startLatch = new CountDownLatch(1); // 모든 스레드가 동시에 시작하도록 설정

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
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드 동시에 시작
        startLatch.countDown();

        // 모든 스레드 완료 대기
        boolean completed = latch.await(30, TimeUnit.SECONDS);
        executorService.shutdown();

        assertEquals(true, completed, "모든 스레드가 시간 내에 완료되어야 합니다.");
        assertEquals(THREAD_COUNT, orderResponses.size(), "모든 주문이 성공적으로 생성되어야 합니다.");

        // 주문 번호의 유일성 검증
        long uniqueOrderNumbers = orderResponses.stream()
                .map(OrderResponse::orderNumber)
                .distinct()
                .count();
        assertEquals(THREAD_COUNT, uniqueOrderNumbers, "모든 주문 번호는 유일해야 합니다.");
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

        // 주문 상품 목록 생성
        List<OrderDetailRequest> orderDetailRequestList = List.of(
                new OrderDetailRequest(21L, 3)  // saleProductId: 1, quantity: 3
        );

        // OrderRequest 객체 생성 및 반환
        return new OrderRequest(addressRequest, orderDetailRequestList);
    }

    @Test
    @DisplayName("재고가 29개인 상품을 10개의 스레드가 3개씩 동시에 구매했을 때 하나의 구매가 실패한다")
    void 테스트_재고() throws InterruptedException {
        // Given
        // 테스트에 사용할 saleProductId (실제 DB에 존재하는 ID 사용 또는 별도 설정 필요)
        Long saleProductId = 21L;

        // 재고를 29개로 설정
        Stock stock = stockRepository.findBySaleProduct_Id(saleProductId).orElseThrow();
        stock.increaseStock(29 - stock.getQuantity()); // 현재 재고를 29개로 맞춤
        stockRepository.save(stock);

        // 실패 카운트
        AtomicInteger failCount = new AtomicInteger(0);

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
                    OrderRequest orderRequest = createSampleOrderRequest(); // 각 주문당 3개 구매

                    try {
                        orderService.createOrder(null, orderRequest); // null은 비회원 주문을 의미
                    } catch (Exception e) {
                        // 재고 부족 등의 예외 발생 시 실패 카운트 증가
                        failCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        // 모든 스레드 시작 신호
        startLatch.countDown();

        // 모든 스레드 완료 대기
        endLatch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();

        // Then
        Stock updatedStock = stockRepository.findBySaleProduct_Id(saleProductId).orElseThrow();

        System.out.println("남은 재고: " + updatedStock.getQuantity());

        // 검증: 하나의 주문이 실패해야 함
        assertEquals(1, failCount.get(), "하나의 주문이 실패해야 합니다");

        // 검증: 재고는 2개가 남아야 함 (29개 - (9개 스레드 * 3개 구매) = 2개)
        assertEquals(2, updatedStock.getQuantity(), "남은 재고는 2개여야 합니다");
    }
}