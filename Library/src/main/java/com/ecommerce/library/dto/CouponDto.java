package com.ecommerce.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CouponDto {
    private Long id;

    private String couponcode;
    private String couponDescription;
    private String offerPercentage;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private LocalDate expireDate;
    private Double minimumOrderAmount;
    private Double maximumOfferAmount;
    private int count;
    private boolean enable;
}
