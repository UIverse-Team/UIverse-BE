package com.jishop.logshop.dto;

import com.jishop.product.domain.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentProductResponse {

    private LocalDate date;
    private List<ProductResponse> products;

    public static RecentProductResponse from(LocalDate date, List<Product> products) {
        List<ProductResponse> list = products.stream()
                .map(ProductResponse::from)
                .toList();

        return new RecentProductResponse(date, list);
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ProductResponse {
        private Long ProductId;
        private String productName;
        private Long mallSeq;
        private String brand;
        private String description;
        private int originPrice;
        private int discountPrice;
        private int discountRate;
        private String mainImage;

        public static ProductResponse from(Product product) {
            return new ProductResponse(
                    product.getId(),
                    product.getProductInfo().getName(),
                    product.getProductInfo().getMallSeq(),
                    product.getProductInfo().getBrand(),
                    product.getProductInfo().getDescription(),
                    product.getProductInfo().getOriginPrice(),
                    product.getProductInfo().getDiscountPrice(),
                    product.getProductInfo().getDiscountRate(),
                    product.getImage().getMainImage()
            );
        }
    }
}
