package com.jishop.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishop.address.dto.AddressRequest;
import com.jishop.category.domain.Category;
import com.jishop.category.repository.CategoryRepository;
import com.jishop.config.TestRedisConfig;
import com.jishop.member.annotation.CurrentUserResolver;
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
import com.jishop.order.repository.OrderRepository;
import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Labels;
import com.jishop.product.domain.Product;
import com.jishop.product.domain.SaleStatus;
import com.jishop.product.repository.ProductRepository;
import com.jishop.review.domain.tag.Tag;
import com.jishop.review.dto.ReviewRequest;
import com.jishop.review.service.ReviewService;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestRedisConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewIntegrationTest {

    @Autowired
    private SaleProductRepository saleProductRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CurrentUserResolver currentUserResolver;

    @BeforeAll
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
    @DisplayName("리뷰 작성")
    void createReview() throws Exception {
        // given
        ReviewRequest request = new ReviewRequest(1L, "굳굳", null, Tag.RECOMMENDED, 3);
        User user = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
        loginResolver(user);

        // when
        Long result = 리뷰작성(request);

        // then
        Assertions.assertNotNull(result);
    }


    @Test
    @DisplayName("한번한 리뷰 작성 또 작성할때 에러 발생.")
    void duplicate_review() throws Exception {
        // given
        ReviewRequest request = new ReviewRequest(1L, "굳굳", null, Tag.RECOMMENDED, 3);
        User user = userRepository.findById(1L).orElseThrow(IllegalStateException::new);
        loginResolver(user);
        리뷰작성(request);

        //when
        mvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
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

    private void loginResolver(User user) throws Exception {
        given(currentUserResolver.supportsParameter(any()))
                .willReturn(true);
        given(currentUserResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(user);// 강제 리턴
    }


    private Long 리뷰작성(ReviewRequest request) throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        return objectMapper.readValue(response, Long.class);
    }
}
