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
