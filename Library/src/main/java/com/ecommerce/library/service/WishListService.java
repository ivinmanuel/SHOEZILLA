package com.ecommerce.library.service;

import com.ecommerce.library.model.WishList;

import java.util.List;

public interface WishListService {

    void addToWishList(String username, Long productId);
    void deleteFromWishList(Long wishListId);
    List<WishList> findWishlistByCustomer(String username);
}
