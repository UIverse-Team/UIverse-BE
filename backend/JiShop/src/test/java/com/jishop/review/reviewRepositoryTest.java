package com.jishop.review;

import com.jishop.address.dto.AddressRequest;
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
import com.jishop.order.dto.OrderDetailRequest;
import com.jishop.order.dto.OrderRequest;
import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.order.repository.OrderRepository;
import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;
import com.jishop.product.domain.SaleStatus;
import com.jishop.product.repository.ProductRepository;
import com.jishop.review.domain.tag.Tag;
import com.jishop.review.dto.ReviewImageResponse;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.dto.ReviewWithOutUserResponse;
import com.jishop.review.repository.ReviewRepository;
import com.jishop.review.service.ReviewService;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class reviewRepositoryTest {

    @Autowired
    private SaleProductRepository saleProductRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @BeforeEach
    void init() {

        Category category = new Category(null, 5000L, "패션", "5000", "패션", 1);
        Category category1 = new Category(category, 5010L, "상의", "5000>5010", "패션>상의", 2);
        Category category2 = new Category(category1, 5100L, "점퍼", "5000>5010>5100", "패션>상의>점퍼", 3);
        categoryRepository.save(category);
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        Product product = new Product(category, 5000L, 5010L, 5100L, "MALL-001", "테스트 상품", "테스트 상품 설명", 10000, 8000, 20, LocalDateTime.now()
                , false, SaleStatus.SELLING, DiscountStatus.NONE, true, "테스트 브랜드", 0, Labels.SPECIAL_PRICE,
                "main.jpg", "image1.jpg", "image2.jpg", "image3.jpg", "image4.jpg", "detail.jpg", 0);

        productRepository.save(product);

        Option option = new Option(OptionCategory.FASHION_CLOTHES, "화이트/FREE", 1000);
        optionRepository.save(option);

        SaleProduct saleProduct = new SaleProduct(product, option, "화이트 점퍼");
        saleProductRepository.save(saleProduct);


        OrderRequest sampleOrderRequest = createSampleOrderRequest();

        User user = User.builder()
                .loginId("testuser@example.com")     // 로그인 아이디 (이메일 또는 소셜 ID)
                .password("testPassword123!")        // 비밀번호 (소셜 로그인은 null 가능)
                .name("홍길동")                       // 이름
                .birthDate("1995-05-01")             // 생년월일
                .gender("M")                         // 성별 (M/F)
                .phone("010-1234-5678")              // 휴대폰 번호
                .provider(LoginType.LOCAL)           // 로그인 타입 (enum 값)
                .ageAgreement(true)                  // 만 14세 이상 동의
                .useAgreement(true)                  // 서비스 이용 약관 동의
                .picAgreement(true)                  // 개인정보 처리 방침 동의
                .adAgreement(false)                  // 광고 수신 동의
                .build();

        userRepository.save(user);

        Order order = Order.from(sampleOrderRequest, user, "1");
        OrderDetail orderDetail = OrderDetail.from(order, saleProduct, 3);
        List<OrderDetail> list = new ArrayList<>();
        list.add(orderDetail);
        order.updateOrderInfo(1000, 0, 1000, list, "1");
        orderRepository.save(order);
    }

    @Test
    @DisplayName("리뷰 작성하고 불러오기")
    void 리뷰_작성밎_조회() throws Exception {
        // given
        ReviewRequest request = new ReviewRequest(1L, "굳굳", null, Tag.RECOMMENDED, 3);
        User user = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
        Long review = reviewService.createReview(request, user);
        //when
        ReviewWithOutUserResponse detailReview = reviewService.getDetailReview(review);
        //then
        Assertions.assertEquals(user.getId(), 1L);
        Assertions.assertEquals(review, 1L);
        Assertions.assertEquals(detailReview.name(), "홍길동");
    }

    @Test
    @DisplayName("리뷰 이미지 슬라이스")
    void imagesSlice() throws Exception {
        // given
        PageRequest pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdAt"));
        Slice<ReviewImageResponse> reviewSlice = reviewRepository.findByAllWithImage(pageable).map(ReviewImageResponse::from);
        //then
        System.out.println(reviewSlice.getContent());
        System.out.println(reviewSlice.getNumber());
        System.out.println(reviewSlice.hasNext());

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
                new OrderDetailRequest(1L, 3)  // saleProductId: 1, quantity: 3
        );

        // OrderRequest 객체 생성 및 반환
        return new OrderRequest(addressRequest, orderDetailRequestList);
    }
}
