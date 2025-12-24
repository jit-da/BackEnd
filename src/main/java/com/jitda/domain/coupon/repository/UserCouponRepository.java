package com.jitda.domain.coupon.repository;

import com.jitda.domain.coupon.entity.UserCoupon;
import com.jitda.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    /**
     * 유저의 쿠폰 개수 조회 (전체)
     */
    long countByUser(User user);

    /**
     * 유저의 사용 가능한 쿠폰 개수 조회 (사용 안 함 + 유효기간 내)
     */
    @Query("SELECT COUNT(uc) FROM UserCoupon uc " +
           "WHERE uc.user = :user " +
           "AND uc.isUsed = false " +
           "AND (uc.coupon.validTo IS NULL OR uc.coupon.validTo >= :now)")
    long countAvailableByUser(@Param("user") User user, @Param("now") LocalDateTime now);

    /**
     * 유저의 쿠폰 목록 조회 (페이징, Coupon과 함께 조회)
     */
    @Query("SELECT uc FROM UserCoupon uc " +
           "LEFT JOIN FETCH uc.coupon " +
           "WHERE uc.user = :user " +
           "ORDER BY uc.createdAt DESC")
    Page<UserCoupon> findByUserWithCoupon(@Param("user") User user, Pageable pageable);

    /**
     * 유저의 사용 가능한 쿠폰 목록 조회 (사용 안 함 + 유효기간 내, 페이징)
     */
    @Query("SELECT uc FROM UserCoupon uc " +
           "LEFT JOIN FETCH uc.coupon " +
           "WHERE uc.user = :user " +
           "AND uc.isUsed = false " +
           "AND (uc.coupon.validTo IS NULL OR uc.coupon.validTo >= :now) " +
           "ORDER BY uc.createdAt DESC")
    Page<UserCoupon> findAvailableByUserWithCoupon(@Param("user") User user, @Param("now") LocalDateTime now, Pageable pageable);

    /**
     * 유저의 쿠폰 목록 조회 (Coupon과 함께 조회, 페이징 없음)
     */
    @Query("SELECT uc FROM UserCoupon uc " +
           "LEFT JOIN FETCH uc.coupon " +
           "WHERE uc.user = :user " +
           "ORDER BY uc.createdAt DESC")
    List<UserCoupon> findAllByUserWithCoupon(@Param("user") User user);
}

