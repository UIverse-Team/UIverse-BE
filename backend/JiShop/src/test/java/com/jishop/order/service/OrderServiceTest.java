//package com.jishop.order.service;
//
//import com.jishop.order.domain.Order;
//import com.jishop.order.domain.OrderDetail;
//import com.jishop.order.domain.OrderNumber;
//import com.jishop.order.domain.OrderStatus;
//import com.jishop.order.dto.OrderDetailRequest;
//import com.jishop.order.dto.OrderRequest;
//import com.jishop.product.domain.Option;
//import com.jishop.product.domain.Product;
//import com.jishop.product.domain.SaleProduct;
//import com.jishop.product.domain.Stock;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class OrderServiceTest {
//
//    @Autowired
//    private EntityManager em;
//
//    @Test
//    public void 주문_생성_테스트() throws Exception {
//        // given
//        Product product = Product.bui;
//        product.setName("테스트 상품");
//        product.setPrice(10000);
//        em.persist(product);
//
//        Option option = new Option();
//        option.setOptionName("옵션1");
//        option.setOptionValue("값1");
//        option.setOptionExtra(0);
//        em.persist(option);
//
//        SaleProduct saleProduct = SaleProduct.builder()
//                .product(product)
//                .option(option)
//                .name("판매 상품")
//                .image("image.jpg")
//                .build();
//        em.persist(saleProduct);
//
//        Stock stock = new Stock();
//        stock.setSaleProduct(saleProduct);
//        stock.setStockNumber(10);
//        em.persist(stock);
//
//        OrderRequest orderRequest = new OrderRequest(
//                "수신자",
//                "010-1234-5678",
//                "기본주소",
//                "상세주소",
//                "12345",
//                List.of(new OrderDetailRequest(saleProduct.getId(), "상품명", 2))
//        );
//
//        // when
//        Order order = orderRequest.toEntity();
//        order.setOrderNumber(OrderNumber.builder().orderNumber("ORD-12345").build());
//        order.setStatus(OrderStatus.ORDER_RECEIVED);
//        order.setTotalPrice(20000);
//        order.setMainProductName("테스트 상품");
//
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        OrderDetail orderDetail = OrderDetail.builder()
//                .order(order)
//                .saleProduct(saleProduct)
//                .quantity(2)
//                .price(10000)
//                .build();
//        orderDetails.add(orderDetail);
//        order.getOrderDetails().addAll(orderDetails);
//
//        em.persist(order);
//
//        // then
//        assertNotNull(order.getId());
//        assertEquals("수신자", order.getReceiver());
//        assertEquals("010-1234-5678", order.getReceiverNumber());
//        assertEquals("기본주소", order.getBaseAddress());
//        assertEquals("상세주소", order.getDetailAddress());
//        assertEquals("12345", order.getZipCode());
//        assertEquals(OrderStatus.ORDER_RECEIVED, order.getStatus());
//        assertEquals("테스트 상품", order.getMainProductName());
//        assertEquals(20000, order.getTotalPrice());
//        assertEquals(1, order.getOrderDetails().size());
//    }
//}