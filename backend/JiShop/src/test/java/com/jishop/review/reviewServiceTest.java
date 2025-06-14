package com.jishop.review;

import com.jishop.option.domain.Option;
import com.jishop.option.domain.OptionCategory;
import com.jishop.order.domain.Order;
import com.jishop.product.domain.Product;
import com.jishop.saleproduct.domain.SaleProduct;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class reviewServiceTest {


    private Option createOption(OptionCategory optionCategory, String optionValue, int optionExtra) {
        return new Option(optionCategory, optionValue, optionExtra);
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

}