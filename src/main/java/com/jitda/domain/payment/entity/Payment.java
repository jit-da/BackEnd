package com.jitda.domain.payment.entity;

import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "method", length = 50)
    private String method;

    @Column(name = "pg_tid", length = 100)
    private String pgTid;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Builder
    private Payment(Order order, int amount, String method, String pgTid, LocalDateTime paidAt) {
        this.order = order;
        this.amount = amount;
        this.method = method;
        this.pgTid = pgTid;
        this.paidAt = paidAt;
    }
}
