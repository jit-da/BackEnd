package com.jitda.api.controller.coupon.dto.response;

import com.jitda.domain.coupon.entity.UserCoupon;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCouponResponse {
    private Long id;
    private CouponResponse coupon;
    private boolean isUsed;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;
    private boolean isAvailable; // 사용 가능 여부 (사용 안 함 + 유효기간 내)

    public static UserCouponResponse of(UserCoupon userCoupon) {
        UserCouponResponse response = new UserCouponResponse();
        response.id = userCoupon.getId();
        response.coupon = CouponResponse.of(userCoupon.getCoupon());
        response.isUsed = userCoupon.isUsed();
        response.usedAt = userCoupon.getUsedAt();
        response.createdAt = userCoupon.getCreatedAt();
        
        // 사용 가능 여부 계산
        LocalDateTime now = LocalDateTime.now();
        response.isAvailable = !userCoupon.isUsed() && 
                              (userCoupon.getCoupon().getValidTo() == null || 
                               userCoupon.getCoupon().getValidTo().isAfter(now));
        
        return response;
    }
}

