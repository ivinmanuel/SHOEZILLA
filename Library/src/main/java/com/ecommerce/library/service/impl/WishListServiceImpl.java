package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.WishList;
import com.ecommerce.library.repository.CustomerRepository;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.repository.WishListRepository;
import com.ecommerce.library.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addToWishList(String username, Long productId) {
        Customer customer = customerRepository.findByEmail(username);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        WishList wishList = new WishList();
        wishList.setCustomer(customer);
        wishList.setProduct(product);
        wishList.setDeleted(false);
        wishListRepository.save(wishList);
    }

    @Override
    public void deleteFromWishList(Long wishListId) {
        wishListRepository.deleteById(wishListId);
    }

    @Override
    public List<WishList> findWishlistByCustomer(String username) {
        Customer customer = customerRepository.findByEmail(username);
        return wishListRepository.findByCustomer(customer);
    }
}
