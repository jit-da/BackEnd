package com.jitda.domain.coupon.entity;

import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_coupons")
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "is_used")
    @ColumnDefault("false")
    private boolean isUsed;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Builder
    private UserCoupon(User user, Coupon coupon, boolean isUsed, LocalDateTime usedAt) {
        this.user = user;
        this.coupon = coupon;
        this.isUsed = isUsed;
        this.usedAt = usedAt;
    }
}
