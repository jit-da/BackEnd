package com.jitda.api.service.coupon.impl;

import com.jitda.api.controller.coupon.dto.response.CouponCountResponse;
import com.jitda.api.controller.coupon.dto.response.UserCouponResponse;
import com.jitda.api.service.coupon.CouponService;
import com.jitda.domain.coupon.entity.UserCoupon;
import com.jitda.domain.coupon.repository.UserCouponRepository;
import com.jitda.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponServiceImpl implements CouponService {

    private final UserCouponRepository userCouponRepository;

    @Override
    public CouponCountResponse getCouponCount(User user) {
        long totalCount = userCouponRepository.countByUser(user);
        long availableCount = userCouponRepository.countAvailableByUser(user, LocalDateTime.now());

        return CouponCountResponse.builder()
                .totalCount(totalCount)
                .availableCount(availableCount)
                .build();
    }

    @Override
    public Page<UserCouponResponse> getUserCoupons(User user, boolean onlyAvailable, Pageable pageable) {
        Page<UserCoupon> userCoupons;
        
        if (onlyAvailable) {
            userCoupons = userCouponRepository.findAvailableByUserWithCoupon(user, LocalDateTime.now(), pageable);
        } else {
            userCoupons = userCouponRepository.findByUserWithCoupon(user, pageable);
        }

        return userCoupons.map(UserCouponResponse::of);
    }

    @Override
    public List<UserCouponResponse> getAllUserCoupons(User user, boolean onlyAvailable) {
        List<UserCoupon> userCoupons;
        
        if (onlyAvailable) {
            // 페이징 없이 사용 가능한 쿠폰만 조회하려면 Repository에 메서드 추가 필요
            // 일단 전체 조회 후 필터링
            userCoupons = userCouponRepository.findAllByUserWithCoupon(user);
            LocalDateTime now = LocalDateTime.now();
            userCoupons = userCoupons.stream()
                    .filter(uc -> !uc.isUsed() && 
                            (uc.getCoupon().getValidTo() == null || uc.getCoupon().getValidTo().isAfter(now)))
                    .collect(Collectors.toList());
        } else {
            userCoupons = userCouponRepository.findAllByUserWithCoupon(user);
        }

        return userCoupons.stream()
                .map(UserCouponResponse::of)
                .collect(Collectors.toList());
    }
}

