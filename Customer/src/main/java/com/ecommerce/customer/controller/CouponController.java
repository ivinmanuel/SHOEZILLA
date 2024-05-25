package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CouponDto;
import com.ecommerce.library.model.Address;
import com.ecommerce.library.model.Coupon;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.ShoppingCart;
import com.ecommerce.library.service.AddressService;
import com.ecommerce.library.service.CouponService;
import com.ecommerce.library.service.CustomerService;
import com.ecommerce.library.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

@Controller
public class CouponController {
    ShoppingCartService shoppingCartService;
    CouponService couponService;
    AddressService addressService;
    CustomerService customerService;

    public CouponController(ShoppingCartService shoppingCartService,CouponService couponService,
                            AddressService addressService, CustomerService customerService) {
        this.shoppingCartService = shoppingCartService;
        this.couponService = couponService;
        this.addressService = addressService;
        this.customerService = customerService;
    }

    @PostMapping("/applyCoupon")
    public String showApplyCoupon(@ModelAttribute("coupon") CouponDto couponDto, Principal principal,
                                  Model model){



        String username=principal.getName();
        Customer customer=customerService.findByEmail(username);
        List<ShoppingCart> shoppingCarts=shoppingCartService.findShoppingCartByCustomer(username);
        Coupon coupon=couponService.findByCouponCode(couponDto.getCouponcode());
        model.addAttribute("coupon",coupon);

        if(coupon.isExpired()==true){
            return "redirect:/checkOut?expired";
        }
        if(coupon==null){
            return "redirect:/checkOut";
        }

        double grandTotal=shoppingCartService.grandTotal(username);
        List<Address> addresses=addressService.findAddressByCustomer(username);
        double payableAmount;
        if(grandTotal<coupon.getMinimumOrderAmount())
        {
            model.addAttribute("errorMessage","order amount is less. ");
            model.addAttribute("addresses",addresses);
            model.addAttribute("cartItem",shoppingCarts);
            model.addAttribute("total",grandTotal);
            model.addAttribute("customer",customer);
            model.addAttribute("payable",grandTotal);
            return "redirect:/checkOut";
        }
        double offerPercentage= Double.parseDouble(coupon.getOfferPercentage());
        double offer= (grandTotal * offerPercentage) / 100;
        if(offer<coupon.getMaximumOfferAmount()) {
            payableAmount  = grandTotal - offer;
        }
        else{
            payableAmount=grandTotal -  coupon.getMinimumOrderAmount();
        }

        couponService.dicreseCoupon(coupon.getId());
        model.addAttribute("addresses",addresses);
        model.addAttribute("cartItem",shoppingCarts);
        model.addAttribute("total",grandTotal);
        model.addAttribute("customer",customer);
        model.addAttribute("payable",payableAmount);
        System.out.println(payableAmount);
        return "checkOut";
    }

    @PostMapping("/removeCoupon")
    public String removeCoupon(Principal principal, Model model) {
        String username = principal.getName();
        Customer customer = customerService.findByEmail(username);
        List<ShoppingCart> shoppingCarts = shoppingCartService.findShoppingCartByCustomer(username);
        double grandTotal = shoppingCartService.grandTotal(username);
        List<Address> addresses = addressService.findAddressByCustomer(username);
        List<Coupon> availableCoupons = couponService.findAll();

        model.addAttribute("addresses", addresses);
        model.addAttribute("cartItem", shoppingCarts);
        model.addAttribute("total", grandTotal);
        model.addAttribute("customer", customer);
        model.addAttribute("payable", grandTotal);
        model.addAttribute("coupons",availableCoupons);

        return "checkOut";
    }


}
