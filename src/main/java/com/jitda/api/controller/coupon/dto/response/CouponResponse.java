package com.jitda.api.controller.coupon.dto.response;

import com.jitda.domain.coupon.entity.Coupon;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponResponse {
    private Long id;
    private String name;
    private String discountType;
    private int discountValue;
    private int minAmount;
    private Integer maxDiscount;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    public static CouponResponse of(Coupon coupon) {
        CouponResponse response = new CouponResponse();
        response.id = coupon.getId();
        response.name = coupon.getName();
        response.discountType = coupon.getDiscountType();
        response.discountValue = coupon.getDiscountValue();
        response.minAmount = coupon.getMinAmount();
        response.maxDiscount = coupon.getMaxDiscount();
        response.validFrom = coupon.getValidFrom();
        response.validTo = coupon.getValidTo();
        return response;
    }
}

