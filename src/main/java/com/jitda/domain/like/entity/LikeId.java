package com.jitda.domain.like.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "product_id")
    private Long productId;
}
