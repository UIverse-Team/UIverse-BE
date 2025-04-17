package com.jishop.product.service.impl;

import com.jishop.order.repository.OrderDetailRepository;
import com.jishop.product.domain.DiscountStatus;
import com.jishop.product.domain.Product;
import com.jishop.product.dto.response.TodaySpecialListResponse;
import com.jishop.product.repository.ProductRepository;
import com.jishop.product.service.ProductDiscountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDiscountServiceImpl implements ProductDiscountService {

    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional(readOnly = true)
    public PagedModel<TodaySpecialListResponse> getProductsByDailyDeal(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Page<Product> productPage = productRepository.findDailyDealProducts(DiscountStatus.DAILY_DEAL, pageable);

        final Page<TodaySpecialListResponse> responsePage = productPage.map(product -> {
            final long totalSales = orderDetailRepository.countTotalOrdersByProductId(product.getId());

            return TodaySpecialListResponse.from(product, totalSales);
        });

        return new PagedModel<>(responsePage);
    }

    @Transactional
    public void updateDailyDeals() {
        int resetCount = productRepository.resetPreviousDailyDeals();
        log.info(" {} 개의 상품이 리셋되었습니다. ", resetCount);

        int updatedCount = productRepository.updateNewDailyDeals();
        log.info(" {}개의 상품이 오늘의 특가로 뽑혔습니다.", updatedCount);
    }
}
