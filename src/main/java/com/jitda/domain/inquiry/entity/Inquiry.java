package com.jitda.domain.inquiry.entity;

import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.product.entity.Product;
import com.jitda.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inquiries")
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Lob
    @Column(name = "question", nullable = false)
    private String question;

    @Lob
    @Column(name = "answer")
    private String answer;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    @Builder
    private Inquiry(User user, Product product, String question, String answer, LocalDateTime answeredAt) {
        this.user = user;
        this.product = product;
        this.question = question;
        this.answer = answer;
        this.answeredAt = answeredAt;
    }
}
