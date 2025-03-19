package com.jishop.storewishlist.service;

import com.jishop.common.exception.DomainException;
import com.jishop.common.exception.ErrorType;
import com.jishop.member.domain.User;
import com.jishop.store.domain.Store;
import com.jishop.store.repository.StoreRepository;
import com.jishop.storewishlist.domain.StoreWishList;
import com.jishop.storewishlist.dto.StoreWishListResponse;
import com.jishop.storewishlist.dto.StoreWishRequest;
import com.jishop.storewishlist.repository.StoreWishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreWishListServiceImpl implements StoreWishListService {

    private final StoreRepository storeRepository;
    private final StoreWishListRepository storeWishListRepository;

    public void addWishStore(User user, StoreWishRequest request) {
        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new DomainException(ErrorType.STORE_NOT_FOUND));

        Optional<StoreWishList> wishStore = storeWishListRepository.findByUserAndStore(user, store);

        if (wishStore.isEmpty()) {
            StoreWishList newWish = request.toEntity(user, store);
            newWish.onStatus();
            storeWishListRepository.save(newWish);
            store.incrementWishCount();
        } else {
            StoreWishList existingWish = wishStore.get();
            existingWish.offStatus();
            storeWishListRepository.delete(existingWish);
            store.decrementWishCount();
        }
    }

    public List<StoreWishListResponse> getWishStores(User user){
        List<StoreWishList> wishList = storeWishListRepository.findAllByUser(user);

        List<Long> storeIds = wishList.stream().map(wish -> wish.getStore().getId()).toList();
        List<Store> stores = storeRepository.findAllById(storeIds);

        return stores.stream().map(StoreWishListResponse::new).toList();
    }
}
