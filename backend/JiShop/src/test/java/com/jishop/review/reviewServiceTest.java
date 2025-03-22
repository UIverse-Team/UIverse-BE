package com.jishop.review;

import com.jishop.category.domain.Category;
import com.jishop.category.repository.CategoryRepository;
import com.jishop.member.domain.LoginType;
import com.jishop.member.domain.User;
import com.jishop.member.repository.UserRepository;
import com.jishop.option.domain.Option;
import com.jishop.option.domain.OptionCategory;
import com.jishop.option.repository.OptionRepository;
import com.jishop.order.domain.Order;
import com.jishop.order.domain.OrderDetail;
import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.order.repository.OrderRepository;
import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;
import com.jishop.product.domain.SaleStatus;
import com.jishop.product.repository.ProductRepository;
import com.jishop.review.domain.Review;
import com.jishop.review.domain.tag.Tag;
import com.jishop.review.dto.LikerIdRequest;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.repository.ReviewRepository;
import com.jishop.review.service.ReviewService;
import com.jishop.reviewproduct.domain.ReviewProduct;
import com.jishop.reviewproduct.repository.ReviewProductRepository;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class reviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewProductRepository reviewProductRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleProductRepository saleProductRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {


    }

    @Test
    @DisplayName("리뷰를 위한 관련 데이터 만들기")
    void data() throws Exception {

//
//        // 카테고리 만들기✅
//
//        // 최상위 카테고리 생성 (예: "전자제품")
//        Category electronics = createCategory(null, 1L, "전자제품", "1", "전자제품", 1);
//
//        // 하위 카테고리 생성 (예: "휴대폰", "노트북")
//        Category smartphones = createCategory(electronics, 2L, "휴대폰", "1 > 2", "전자제품 > 휴대폰", 2);
//        Category laptops = createCategory(electronics, 3L, "노트북", "1 > 3", "전자제품 > 노트북", 2);
//        electronics.addChildCategory(smartphones);
//        electronics.addChildCategory(laptops);
//
//        // 하위 카테고리 추가 (예: "갤럭시", "아이폰")
//        Category galaxy = createCategory(smartphones, 4L, "갤럭시", "1 > 2 > 4", "전자제품 > 휴대폰 > 갤럭시", 3);
//        Category iphone = createCategory(smartphones, 5L, "아이폰", "1 > 2 > 5", "전자제품 > 휴대폰 > 아이폰", 3);
//        smartphones.addChildCategory(galaxy);
//        smartphones.addChildCategory(iphone);
//
//        categoryRepository.save(electronics);
//        System.out.println("✅ 카테고리 데이터가 성공적으로 저장되었습니다!");
//        // 옵션 넣기✅
//        Option g_option_blue = createOption(OptionCategory.ELECTRONICS_DIGITAL_DEVICE, "블루/xl", 0);
//        Option g_option_red = createOption(OptionCategory.ELECTRONICS_DIGITAL_DEVICE, "레드/xl", 0);
//        Option i_option_blue = createOption(OptionCategory.ELECTRONICS_DIGITAL_DEVICE, "블루/l", 0);
//        Option i_option_red = createOption(OptionCategory.ELECTRONICS_DIGITAL_DEVICE, "레드/l", 0);
//        Option g_optoin_gold = createOption(OptionCategory.ELECTRONICS_DIGITAL_DEVICE, "골드/xl", 0);
//
//        // ✅ 한 번의 saveAll로 저장 최적화
//        optionRepository.saveAll(List.of(g_option_blue, g_option_red, g_optoin_gold, i_option_blue, i_option_red));
//
//        System.out.println("✅ 옵션 데이터가 성공적으로 저장되었습니다!");
//
//        // 상품 넣기
//        // ✅ 샘플 상품 데이터 생성
//        Product laptop = createProduct(iphone, "L001", "M001", "S001", "MALL123",
//                "맥북 프로 16인치", "애플 맥북 프로 16인치, M3 프로세서 탑재", 3000000, 2700000,
//                LocalDateTime.of(2024, 3, 1, 0, 0), false, SaleStatus.SELLING, DiscountStatus.NONE,
//                true, "Apple", 0, Labels.NONE,
//                "macbook_main.jpg", "macbook_1.jpg", "macbook_2.jpg", "macbook_3.jpg", "macbook_4.jpg",
//                "macbook_detail.jpg", 0);
//
//        Product smartphone = createProduct(iphone, "L001", "M002", "S002", "MALL124",
//                "갤럭시 S24 울트라", "삼성 최신 갤럭시 S24 울트라, AI 카메라 탑재", 1500000, 1350000,
//                LocalDateTime.of(2024, 2, 10, 0, 0), false, SaleStatus.SELLING, DiscountStatus.NONE,
//                true, "Samsung", 0, Labels.NONE,
//                "galaxy_main.jpg", "galaxy_1.jpg", "galaxy_2.jpg", "galaxy_3.jpg", "galaxy_4.jpg",
//                "galaxy_detail.jpg", 0);
//
//        Product tablet = createProduct(galaxy, "L001", "M003", "S003", "MALL125",
//                "아이패드 프로 12.9", "M2 칩셋 탑재, 전문가용 태블릿", 1800000, 1650000,
//                LocalDateTime.of(2024, 1, 15, 0, 0), false, SaleStatus.SELLING, DiscountStatus.NONE,
//                false, "Apple", 0, Labels.NONE,
//                "ipad_main.jpg", "ipad_1.jpg", "ipad_2.jpg", "ipad_3.jpg", "ipad_4.jpg",
//                "ipad_detail.jpg", 0);
//
//        // ✅ 한 번의 saveAll() 호출로 최적화 저장
//        productRepository.saveAll(List.of(laptop, smartphone, tablet));
//
//        System.out.println("✅ 상품 데이터가 성공적으로 저장되었습니다!");
//
//        // 판매 상품 넣기
//        SaleProduct saleProduct1 = createSaleProduct(tablet, g_option_blue);
//        SaleProduct saleProduct2 = createSaleProduct(tablet, g_option_red);
//        SaleProduct saleProduct3 = createSaleProduct(tablet, i_option_red);
//        SaleProduct saleProduct4 = createSaleProduct(tablet, i_option_blue);
//        SaleProduct saleProduct5 = createSaleProduct(smartphone, g_optoin_gold);
//
//        // ✅ 한 번의 saveAll() 호출로 최적화 저장
//        saleProductRepository.saveAll(List.of(saleProduct1, saleProduct2, saleProduct3, saleProduct4, saleProduct5));
//
//        System.out.println("✅ 판매상품 데이터가 성공적으로 저장되었습니다!");
//
//        // 주문 넣기 & 주문 상세 넣기✅
//
//        //  주문 생성 (1번 사용자, 결제 ID 1001L)
//        Order order1 = createOrder(1001L, 1L, "아이폰 14 프로", 2500000,
//                "김철수", "010-1234-5678", "12345", "서울특별시 강남구", "테헤란로 123");
//
//        // ✅ 주문 상세 생성 (주문 상품 연결)
//        List<OrderDetail> orderDetails1 = List.of(
//                createOrderDetail(order1, saleProduct1, 2, 1250000), // 상품 2개
//                createOrderDetail(order1, saleProduct2, 1, 500000)   // 상품 1개
//        );
//        order1.updateOrderInfo("아이폰 14 프로", 2500000, orderDetails1, generateOrderNumber());
//
//        // ✅ 주문 생성 (2번 사용자, 결제 ID 1002L)
//        Order order2 = createOrder(1002L, 2L, "갤럭시 S24 울트라", 1700000,
//                "이영희", "010-9876-5432", "54321", "부산광역시 해운대구", "해운대로 456");
//
//        // ✅ 주문 상세 생성
//        List<OrderDetail> orderDetails2 = List.of(
//                createOrderDetail(order2, saleProduct2, 1, 1700000),  // 상품 1개
//                createOrderDetail(order2, saleProduct5, 1, 1700000)  // 상품 1개
//        );
//        order2.updateOrderInfo("갤럭시 S24 울트라", 1700000, orderDetails2, generateOrderNumber());
//
//        Order order3 = createOrder(1003L, 3L, "갤럭시 탭", 100,
//                "이씨", "010-0000-0000", "23443", "경기도", "모란로");
//
//        List<OrderDetail> orderDetails3 = List.of(
//                createOrderDetail(order3, saleProduct2, 2, 50),
//                createOrderDetail(order3, saleProduct3, 2, 50)
//        );
//
//        order3.updateOrderInfo("갤럭시 탭", 100, orderDetails3, generateOrderNumber());
//        // ✅ 한 번의 saveAll() 호출로 최적화 저장
//        orderRepository.saveAll(List.of(order1, order2, order3));
//
//        System.out.println("✅ 주문 데이터가 성공적으로 저장되었습니다!");
//        // 회원 넣기
//        User user1 = createUser("user1@example.com", "password123", "김철수", "1990-05-20", "MALE", "010-1234-5678", LoginType.GOOGLE);
//        User user2 = createUser("user2@example.com", null, "이영희", "1995-10-10", "FEMALE", "010-9876-5432", LoginType.GOOGLE);
//        User user3 = createUser("user3@example.com", "securePass!", "박민수", "1988-03-15", "MALE", "010-5555-6666", LoginType.KAKAO);
//
//        // ✅ 한 번의 saveAll() 호출로 최적화 저장
//        userRepository.saveAll(List.of(user1, user2, user3));
//
//        System.out.println("✅ 회원 데이터가 성공적으로 저장되었습니다!");
    }

    @Test
    @DisplayName("리뷰 저장")
    void create() throws Exception {
        // given
        ReviewRequest request1 = getReviewRequest(1L, "This is a test", Tag.NEUTRAL, 5);
        ReviewRequest request2 = getReviewRequest(2L, "새로운 테스트", Tag.RECOMMENDED, 5);
        ReviewRequest request3 = getReviewRequest(3L, "물까 1", Tag.RECOMMENDED, 3);
        ReviewRequest request4 = getReviewRequest(4L, "물까 2", Tag.RECOMMENDED, 3);
        ReviewRequest request5 = getReviewRequest(5L, "물까 3", Tag.RECOMMENDED, 1);
        ReviewRequest request6 = getReviewRequest(6L, "물까 4", Tag.RECOMMENDED, 3);
        List<String> images = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            images.add("이미지" + i + ".jpg");
        }
        reviewService.createReview(request1, images, 1L);
        reviewService.createReview(request2, images, 1L);
        reviewService.createReview(request3, images, 2L);
        reviewService.createReview(request4, images, 3L);
        reviewService.createReview(request5, images, 3L);
        reviewService.createReview(request6, images, 3L);
    }

    @Test
    @DisplayName("상품에서 조회 - 비회원일때")
    void getproduct_notUser() throws Exception {
        // given
        Long productId = 3L;

        System.out.println("dkfj");

        PageRequest pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        PagedModel<?> productReviews = reviewService.getProductReviewsWithoutUser(productId, pageable);
        //when

        //then
        productReviews.getContent().forEach(System.out::println);
    }

    @Test
    @DisplayName("상품에서 조회 - 회원일때")
    void getproduct_user() throws Exception {
        // given
        Long productId = 3L;
        Long userId = 2L;

        PageRequest pageable = PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "createdAt"));
        PagedModel<?> productReviews = reviewService.getProductReviewsWithUser(productId, userId, pageable);
        //when

        //then
        productReviews.getContent().forEach(System.out::println);
    }

    @Test
    @DisplayName("리뷰 좋아요 테스트")
    void review_like() throws Exception {
        // given
        /**
         * 뭘 줘야 할까?
         * 리뷰할 id, 리뷰하는 유저 id
         * 리뷰 좋아요 테이블도 만들고
         *
         */
        Long reviewId = 1L;

        Long userId = 1L;
        LikerIdRequest likerId = new LikerIdRequest(userId);

        reviewService.likeReview(likerId, reviewId);
        //when

        //then

    }


    @Test
    @DisplayName("리뷰 좋아요 취소 테스트")
    void review_unlike() throws Exception {
        // given
        /**
         * 뭘 줘야 할까?
         * 리뷰할 id, 리뷰하는 유저 id
         * 리뷰 좋아요 테이블도 만들고
         *
         */
        Long reviewId = 1L;

        Long userId = 1L;
        LikerIdRequest likerId = new LikerIdRequest(userId);

        reviewService.unlikeReview(likerId, reviewId);
        //when

        //then

    }

    @Test
    @DisplayName("리뷰 삭제")
    void reviewDelete() throws Exception {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        reviewService.deleteReview(reviewId, userId);

        //when

        //then
    }

    @Test
    @DisplayName("리뷰 삭제 동시성 테스트")
    void review_delete() throws Exception {
        // given

        int threadCount = 55; // 5개의 스레드에서 동시에 삭제 요청
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        Long userId = 2L;
        Long reviewId = 9L;

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    reviewService.deleteReview(reviewId, userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 실행될 때까지 대기

        executorService.shutdown();
        ReviewProduct rp = reviewProductRepository.findByProductId(3L);
        assertThat(rp.getReviewCount()).isEqualTo(2);
        assertThat(rp.getReviewScore()).isEqualTo(4);

        //when

        //then

    }

    private ReviewRequest getReviewRequest(Long orderDetailId, String content, Tag tag, int rating) {
        return new ReviewRequest(orderDetailId, content, tag, rating);
    }

    private Product createProduct(Category category, String lCatId, String mCatId, String sCatId,
                                  String mallSeq, String name, String description, int originPrice,
                                  int discountPrice, LocalDateTime manufactureDate, Boolean secret,
                                  SaleStatus saleStatus, DiscountStatus discountStatus, Boolean isDiscount,
                                  String brand, int wishListCount, Labels labels,
                                  String mainImage, String image1, String image2, String image3, String image4,
                                  String detailImage, int productViewCount) {
        return Product.builder()
                .category(category)
                .lCatId(lCatId)
                .mCatId(mCatId)
                .sCatId(sCatId)
                .mallSeq(mallSeq)
                .name(name)
                .description(description)
                .originPrice(originPrice)
                .discountPrice(discountPrice)
                .manufactureDate(manufactureDate)
                .secret(secret)
                .saleStatus(saleStatus)
                .discountStatus(discountStatus)
                .isDiscount(isDiscount)
                .brand(brand)
                .wishListCount(wishListCount)
                .labels(labels)
                .mainImage(mainImage)
                .image1(image1)
                .image2(image2)
                .image3(image3)
                .image4(image4)
                .detailImage(detailImage)
                .productViewCount(productViewCount)
                .build();
    }

    private Option createOption(OptionCategory optionCategory, String optionValue, int optionExtra) {
        return new Option(optionCategory, optionValue, optionExtra);
    }

    private static Category createCategory(Category category, Long currentId,
                                           String name, String wholeCategoryId,
                                           String wholeCategoryName, int level) {
        return Category.builder()
                .parent(category)  // 최상위 카테고리이므로 부모 없음
                .currentId(currentId)
                .name(name)
                .wholeCategoryId(wholeCategoryId)
                .wholeCategoryName(wholeCategoryName)
                .level(level)
                .build();
    }

    private SaleProduct createSaleProduct(Product product, Option option) {
        return SaleProduct.builder().
                product(product).
                option(option)
                .build();
    }

    private Order createOrder(Long paymentId, Long userId, String mainProductName, int totalPrice,
                              String receiver, String receiverNumber, String zipCode,
                              String baseAddress, String detailAddress) {
        return Order.builder()
                .totalPrice(totalPrice)
                .recipient(receiver)
                .phone(receiverNumber)
                .zonecode(zipCode)
                .address(baseAddress)
                .detailAddress(detailAddress)
                .build();
    }

    private OrderDetail createOrderDetail(Order order, SaleProduct saleProduct, int quantity, int price) {
        return OrderDetail.builder()
                .order(order)
                .saleProduct(saleProduct)
                .quantity(quantity)
                .orderPrice(price)
                .build();
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + UUID.randomUUID().toString();
    }

    private User createUser(String loginId, String password, String name, String birthDate, String gender, String phone, LoginType provider) {
        return User.builder()
                .loginId(loginId)
                .password(password)  // null 가능
                .name(name)
                .birthDate(birthDate)
                .gender(gender)
                .phone(phone)
                .provider(provider)
                .build();
    }
}
