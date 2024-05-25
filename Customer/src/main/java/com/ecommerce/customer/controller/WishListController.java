package com.ecommerce.customer.controller;

import com.ecommerce.library.model.WishList;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.WishListService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/wishlist")
public class WishListController {


    private WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/wishList")
    public String showWishList(Principal principal, Model model, HttpSession httpSession) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        List<WishList> wishLists = wishListService.findWishlistByCustomer(username);
        model.addAttribute("wishlists", wishLists);
        return "wishlist";
    }

    @GetMapping("/addToWishList")
    public String addToWishList(@RequestParam("productId") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        wishListService.addToWishList(username, id);
        return "redirect:/wishlist/wishList";
    }

    @GetMapping("/removeFromWishList/{id}")
    public String removeFromWishList(@PathVariable("id") Long id) {
        wishListService.deleteFromWishList(id);
        return "redirect:/wishlist/wishList";
    }
}


