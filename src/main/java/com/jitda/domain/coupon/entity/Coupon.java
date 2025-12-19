package com.jitda.domain.coupon.entity;

import com.jitda.domain.common.BaseEntity;
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
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "discount_type", length = 20)
    private String discountType;

    @Column(name = "discount_value", nullable = false)
    private int discountValue;

    @Column(name = "min_amount")
    @ColumnDefault("0")
    private int minAmount;

    @Column(name = "max_discount")
    private Integer maxDiscount;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Builder
    private Coupon(String name, String discountType, int discountValue, int minAmount, Integer maxDiscount, LocalDateTime validFrom, LocalDateTime validTo) {
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minAmount = minAmount;
        this.maxDiscount = maxDiscount;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
}
