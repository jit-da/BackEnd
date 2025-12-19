package com.jitda.domain.seller.entity;

import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sellers")
public class Seller extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seller_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    @Column(name = "business_number", nullable = false, length = 20)
    private String businessNumber;

    @Column(name = "opened_at")
    private LocalDate openedAt;

    @Column(name = "ceo_name", nullable = false, length = 100)
    private String ceoName;

    @Builder
    private Seller(User user, String companyName, String businessNumber, LocalDate openedAt, String ceoName) {
        this.user = user;
        this.companyName = companyName;
        this.businessNumber = businessNumber;
        this.openedAt = openedAt;
        this.ceoName = ceoName;
    }
}
