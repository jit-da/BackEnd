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
@Table(name = "product_option_values")
public class ProductOptionValue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_value_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption productOption;

    @Column(name = "value_name", nullable = false, length = 100)
    private String valueName;

    @Column(name = "price_add")
    @ColumnDefault("0")
    private int priceAdd;

    @Column(name = "stock")
    @ColumnDefault("0")
    private int stock;

    @Builder
    private ProductOptionValue(ProductOption productOption, String valueName, int priceAdd, int stock) {
        this.productOption = productOption;
        this.valueName = valueName;
        this.priceAdd = priceAdd;
        this.stock = stock;
    }
}
