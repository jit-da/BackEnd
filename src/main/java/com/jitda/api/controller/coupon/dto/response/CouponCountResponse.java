package com.jitda.api.controller.coupon.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponCountResponse {
    private long totalCount;      // 전체 쿠폰 개수
    private long availableCount;  // 사용 가능한 쿠폰 개수
}

