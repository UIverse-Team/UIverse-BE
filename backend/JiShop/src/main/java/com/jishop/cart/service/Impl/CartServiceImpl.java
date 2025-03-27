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
                .map(this::mapToCartDetailResponse)
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

        //기존 장바구니에 있는 수량 확인
        int existingQuantity = (cart != null) ? cart.getQuantity() : 0;

        // 요청 수량 + 기존 수량이 재고보다 많으면 예외
        if(!stock.hasStock(existingQuantity + requestQuantity))
            throw new DomainException(ErrorType.INSUFFICIENT_STOCK);


        //이미 장바구니에 있으면 수량 업데이트!
        if(cart != null){
            cart.updateQuantity(addCartRequest.quantity());
        } else {
            cart = Cart.builder()
                    .user(user)
                    .saleProduct(saleProduct)
                    .quantity(addCartRequest.quantity())
                    .build();
            cartRepository.save(cart);
        }
        return mapToCartDetailResponse(cart);
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

        return mapToCartDetailResponse(cart);
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
                            saleProduct.getProduct().getName(),
                            saleProduct.getOption() != null ? saleProduct.getOption().getOptionValue() : "기본옵션",
                            saleProduct.getProduct().getDiscountPrice(),
                            saleProduct.getProduct().getOriginPrice(),
                            saleProduct.getProduct().getOriginPrice() - saleProduct.getProduct().getDiscountPrice(),
                            quantity,
                            saleProduct.getProduct().getDiscountPrice(), //수량 1일 때 판매가격
                            saleProduct.getProduct().getMainImage(),
                            saleProduct.getProduct().getBrand()
                    );
                })
                .toList();

        return CartResponse.of(cartDetailResponses);
    }

    private CartDetailResponse mapToCartDetailResponse(Cart cart) {
        return new CartDetailResponse(
                cart.getId(),
                cart.getSaleProduct().getId(),
                cart.getSaleProduct().getProduct().getName(),
                cart.getSaleProduct().getOption() != null ? cart.getSaleProduct().getOption().getOptionValue() : "기본옵션",
                cart.getSaleProduct().getProduct().getDiscountPrice(),
                cart.getSaleProduct().getProduct().getOriginPrice(),
                cart.getSaleProduct().getProduct().getOriginPrice()-cart.getSaleProduct().getProduct().getDiscountPrice(),
                cart.getQuantity(),
                cart.getSaleProduct().getProduct().getDiscountPrice() * cart.getQuantity(),
                cart.getSaleProduct().getProduct().getMainImage(),
                cart.getSaleProduct().getProduct().getBrand()
        );
    }
}
