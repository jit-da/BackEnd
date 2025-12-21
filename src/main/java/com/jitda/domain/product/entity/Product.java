package com.jitda.domain.product.entity;

import com.jitda.domain.category.entity.CategoryMedium;
import com.jitda.domain.category.entity.CategorySmall;
import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.seller.entity.Seller;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "products")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "discount_rate", precision = 5, scale = 2)
    @ColumnDefault("0.00")
    private BigDecimal discountRate;

    @Column(name = "discount_price")
    @ColumnDefault("0")
    private int discountPrice;

    @Column(name = "is_option")
    @ColumnDefault("false")
    private boolean isOption;

    @Column(name = "stock")
    @ColumnDefault("0")
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "large_id")
    private com.jitda.domain.category.entity.CategoryLarge categoryLarge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medium_id")
    private CategoryMedium categoryMedium;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "small_id")
    private CategorySmall categorySmall;

    @Builder
    private Product(Seller seller, String name, String description, int price, BigDecimal discountRate, int discountPrice, boolean isOption, int stock, com.jitda.domain.category.entity.CategoryLarge categoryLarge, CategoryMedium categoryMedium, CategorySmall categorySmall) {
        this.seller = seller;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountRate = discountRate;
        this.discountPrice = discountPrice;
        this.isOption = isOption;
        this.stock = stock;
        this.categoryLarge = categoryLarge;
        this.categoryMedium = categoryMedium;
        this.categorySmall = categorySmall;
    }

    public Long getId() {
        return id;
    }
}
