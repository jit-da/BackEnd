package com.jitda.api.service.coupon;

import com.jitda.api.controller.coupon.dto.response.CouponCountResponse;
import com.jitda.api.controller.coupon.dto.response.UserCouponResponse;
import com.jitda.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponService {
    /**
     * 유저의 쿠폰 개수 조회
     */
    CouponCountResponse getCouponCount(User user);

    /**
     * 유저의 쿠폰 목록 조회 (페이징)
     * @param onlyAvailable true면 사용 가능한 쿠폰만 조회
     */
    Page<UserCouponResponse> getUserCoupons(User user, boolean onlyAvailable, Pageable pageable);

    /**
     * 유저의 쿠폰 목록 조회 (전체, 페이징 없음)
     * @param onlyAvailable true면 사용 가능한 쿠폰만 조회
     */
    List<UserCouponResponse> getAllUserCoupons(User user, boolean onlyAvailable);
}

