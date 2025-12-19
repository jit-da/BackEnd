package com.jitda.domain.like.entity;

import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.product.entity.Product;
import com.jitda.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes")
public class Like extends BaseEntity {

    @EmbeddedId
    private LikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    private Like(User user, Product product) {
        this.id = new LikeId(user.getId(), product.getId());
        this.user = user;
        this.product = product;
    }
}
