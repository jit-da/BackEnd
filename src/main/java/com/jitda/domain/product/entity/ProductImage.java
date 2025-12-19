package com.jitda.domain.product.entity;

import com.jitda.domain.common.BaseEntity;
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
@Table(name = "product_images")
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "is_main")
    @ColumnDefault("false")
    private boolean isMain;

    @Column(name = "sort_order")
    @ColumnDefault("1")
    private int sortOrder;

    @Builder
    private ProductImage(Product product, String imageUrl, boolean isMain, int sortOrder) {
        this.product = product;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
        this.sortOrder = sortOrder;
    }
}
