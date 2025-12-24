package com.jitda.api.controller.coupon;

import com.jitda.api.controller.coupon.dto.response.CouponCountResponse;
import com.jitda.api.controller.coupon.dto.response.UserCouponResponse;
import com.jitda.api.service.coupon.CouponService;
import com.jitda.global.config.oauth2.CustomOAuth2User;
import com.jitda.global.response.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "쿠폰")
@RequestMapping("/api/v1/coupons")
public class CouponControllerV1 {

    private final CouponService couponService;

    /**
     * ✅ 유저의 쿠폰 개수 조회
     * GET /api/v1/coupons/count
     */
    @GetMapping("/count")
    @Operation(summary = "쿠폰 개수 조회", description = "유저가 가진 전체 쿠폰 개수와 사용 가능한 쿠폰 개수를 조회합니다.")
    public ApiResponse<CouponCountResponse> getCouponCount(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("CouponControllerV1.getCouponCount - userId: {}", customOAuth2User.getUser().getId());

        CouponCountResponse response = couponService.getCouponCount(customOAuth2User.getUser());
        return ApiResponse.ok(response);
    }

    /**
     * ✅ 유저의 쿠폰 목록 조회 (페이징)
     * GET /api/v1/coupons?onlyAvailable=true&page=0&size=10
     */
    @GetMapping
    @Operation(summary = "쿠폰 목록 조회", description = "유저가 가진 쿠폰 목록을 조회합니다. 페이징 지원.")
    public ApiResponse<Page<UserCouponResponse>> getUserCoupons(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Parameter(description = "사용 가능한 쿠폰만 조회 (기본값: false)")
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "10") int size) {
        log.info("CouponControllerV1.getUserCoupons - userId: {}, onlyAvailable: {}, page: {}, size: {}", 
                customOAuth2User.getUser().getId(), onlyAvailable, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<UserCouponResponse> response = couponService.getUserCoupons(customOAuth2User.getUser(), onlyAvailable, pageable);
        return ApiResponse.ok(response);
    }

    /**
     * ✅ 유저의 쿠폰 목록 조회 (전체, 페이징 없음)
     * GET /api/v1/coupons/all?onlyAvailable=true
     */
    @GetMapping("/all")
    @Operation(summary = "쿠폰 전체 목록 조회", description = "유저가 가진 모든 쿠폰 목록을 조회합니다. 페이징 없음.")
    public ApiResponse<List<UserCouponResponse>> getAllUserCoupons(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Parameter(description = "사용 가능한 쿠폰만 조회 (기본값: false)")
            @RequestParam(defaultValue = "false") boolean onlyAvailable) {
        log.info("CouponControllerV1.getAllUserCoupons - userId: {}, onlyAvailable: {}", 
                customOAuth2User.getUser().getId(), onlyAvailable);

        List<UserCouponResponse> response = couponService.getAllUserCoupons(customOAuth2User.getUser(), onlyAvailable);
        return ApiResponse.ok(response);
    }
}

