package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.CouponDto;
import com.ecommerce.library.model.Coupon;
import com.ecommerce.library.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CouponController {

    private CouponService couponService;
    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/coupons")
    public String showCoupon(Model model){

        List<Coupon> coupons=couponService.findAll();

        model.addAttribute("coupons",coupons);
        return "coupons";
    }
    @GetMapping("/add-coupon")
    public String ShowAddCoupon(Model model){
        CouponDto couponDto=new CouponDto();
        model.addAttribute("coupon",couponDto);
        return "add-coupon";
    }

    @PostMapping("/saveCoupon")
    public String showSaveCoupon(@ModelAttribute("coupon")CouponDto couponDto){
        couponService.save(couponDto);

        return "redirect:/coupons";
    }

    @GetMapping("/disableCoupon")
    public String showDisableCoupon(@RequestParam("couponId")Long id){

        couponService.disableCoupon(id);
        return "redirect:/coupons";
    }

    @GetMapping("/enableCoupon")
    public String showEnableCoupon(@RequestParam("couponId")Long id){

        couponService.enableCoupon(id);
        return "redirect:/coupons";
    }
    @GetMapping("/editCoupon")
    public String showCouponUpdate(@RequestParam("couponId")Long id,Model model){
        Coupon coupon=couponService.findByid(id);
        model.addAttribute("coupon",coupon);
        return "update-coupon";
    }
    @PostMapping("/update-coupon")
    public String updateCoupon(@ModelAttribute("coupon")CouponDto couponDto){
        couponService.updateCoupon(couponDto);
        return "redirect:/coupons";
    }

    @GetMapping("/deleteCoupon")
    public String showCouponDelete (@RequestParam("couponId")Long id){
        couponService.deleteCoupon(id);
        return "redirect:/coupons";
    }
}
