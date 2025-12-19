package com.jitda.domain.order.entity;

import com.jitda.domain.address.entity.Address;
import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @Column(name = "discount_total")
    @ColumnDefault("0")
    private int discountTotal;

    @Column(name = "final_amount", nullable = false)
    private int finalAmount;

    @Column(name = "shipping_fee", nullable = false)
    private int shippingFee;

    @Builder
    private Order(User user, Address address, int totalAmount, int discountTotal, int finalAmount, int shippingFee) {
        this.user = user;
        this.address = address;
        this.totalAmount = totalAmount;
        this.discountTotal = discountTotal;
        this.finalAmount = finalAmount;
        this.shippingFee = shippingFee;
    }
}
