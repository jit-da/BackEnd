package com.jitda.domain.category.entity;

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
@Table(name = "categories_small")
public class CategorySmall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "small_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medium_id", nullable = false)
    private CategoryMedium categoryMedium;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "shipping_fee", nullable = false)
    @ColumnDefault("0")
    private int shippingFee;

    @Builder
    private CategorySmall(CategoryMedium categoryMedium, String name, int shippingFee) {
        this.categoryMedium = categoryMedium;
        this.name = name;
        this.shippingFee = shippingFee;
    }
}
