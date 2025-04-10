package com.jishop.cart.service.Impl;

import com.jishop.cart.domain.Cart;
import com.jishop.cart.dto.*;
import com.jishop.cart.repository.CartRepository;
import com.jishop.cart.service.CartService;
import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.saleproduct.domain.SaleProduct;
import com.jishop.saleproduct.repository.SaleProductRepository;
import com.jishop.stock.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final SaleProductRepository saleProductRepository;

    //장바구니 불러오기
    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(User user) {
        List<Cart> carts = cartRepository.findCartsWithProductAndOptionByUser(user);
        List<CartDetailResponse> cartDetailResponses = carts.stream()
                .map(cart -> CartDetailResponse.of(cart, false))
                .toList();

        return CartResponse.of(cartDetailResponses);
    }

    //장바구니 상품 추가
    @Override
    @Transactional
    public CartDetailResponse addCartItem(User user, AddCartRequest addCartRequest) {
        SaleProduct saleProduct = saleProductRepository.findById(addCartRequest.saleProductId())
                .orElseThrow(()->new DomainException(ErrorType.PRODUCT_NOT_FOUND));

        int requestQuantity = addCartRequest.quantity();
        Stock stock = saleProduct.getStock();

        Cart cart = cartRepository.findByUserAndSaleProduct(user, saleProduct).orElse(null);

        // 이미 장바구니에 상품이 존재하는 경우
        if (cart != null) {
            // isForced가 false인 경우, 기존 상품 정보만 반환하고 수량은 업데이트하지 않음
            if (!addCartRequest.isForced()) {
                return CartDetailResponse.of(cart, true);
            }

            // isForced가 true인 경우, 기존 수량 + 새로운 수량으로 업데이트
            int totalQuantity = cart.getQuantity() + requestQuantity;

            // 재고 체크
            if(!stock.hasStock(totalQuantity))
                throw new DomainException(ErrorType.INSUFFICIENT_STOCK);

            cart.updateQuantity(requestQuantity);
        } else {
            // 장바구니에 상품이 없는 경우
            // 재고 체크
            if(!stock.hasStock(requestQuantity))
                throw new DomainException(ErrorType.INSUFFICIENT_STOCK);

            cart = Cart.builder()
                    .user(user)
                    .saleProduct(saleProduct)
                    .quantity(requestQuantity)
                    .build();
            cartRepository.save(cart);
        }

        return CartDetailResponse.of(cart, false);
    }

    // 장바구니 수량 수정
    @Override
    @Transactional
    public CartDetailResponse updateCart(User user, UpdateCartRequest updateCartRequest) {
        Cart cart = cartRepository.findById(updateCartRequest.cartId())
                .orElseThrow(()-> new DomainException(ErrorType.CART_ITEM_NOT_FOUND));

        Stock stock = cart.getSaleProduct().getStock();

        if(!stock.hasStock(updateCartRequest.quantity()))
            throw new DomainException(ErrorType.INSUFFICIENT_STOCK);

        cart.updateQuantity(updateCartRequest.quantity());

        return CartDetailResponse.of(cart, false);
    }

    //장바구니 상품 삭제
    @Override
    public void removeCartItem(User user, DeleteCartRequest deleteCartRequest) {
        List<Cart> cart = cartRepository.findAllById(deleteCartRequest.cartIdList());

        if(cart.isEmpty())
            throw new DomainException(ErrorType.CART_ITEM_NOT_FOUND);

        cartRepository.deleteAll(cart);
    }

    //비회원 장바구니 조회
    @Override
    public CartResponse getGuestCart(List<GuestCartRequest> guestCartRequests) {

        //아이디만 추출
        List<Long> saleProductIds = guestCartRequests.stream()
                .map(GuestCartRequest::id)
                .toList();

        List<SaleProduct> saleProducts = saleProductRepository.findAllByIdWithProductAndOptionAndStock(saleProductIds);

        Map<Long, Integer> quantityMap = guestCartRequests.stream()
                .collect(Collectors.toMap(GuestCartRequest::id, GuestCartRequest::quantity));

        List<CartDetailResponse> cartDetailResponses = saleProducts.stream()
                .map(saleProduct -> {
                    Integer quantity = quantityMap.get(saleProduct.getId());

                    if(quantity == null)
                        throw new DomainException(ErrorType.PRODUCT_NOT_FOUND);

                    return new CartDetailResponse(
                            null, //장바구니 ID는 null (비회원이니까)
                            saleProduct.getId(),
                            saleProduct.getProduct().getProductInfo().getName(),
                            saleProduct.getOption() != null ? saleProduct.getOption().getOptionValue() : "기본옵션",
                            saleProduct.getProduct().getProductInfo().getDiscountPrice(),
                            saleProduct.getProduct().getProductInfo().getOriginPrice(),
                            saleProduct.getProduct().getProductInfo().getOriginPrice() -
                                    saleProduct.getProduct().getProductInfo().getDiscountPrice(),
                            quantity,
                            saleProduct.getProduct().getProductInfo().getDiscountPrice() * quantity,
                            saleProduct.getProduct().getImage().getMainImage(),
                            saleProduct.getProduct().getProductInfo().getBrand(),
                            false
                    );
                })
                .toList();

        return CartResponse.of(cartDetailResponses);
    }
}