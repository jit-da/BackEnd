package com.jitda.domain.order.entity;

import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.product.entity.Product;
import com.jitda.domain.product.entity.ProductOptionValue;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_value_id")
    private ProductOptionValue productOptionValue;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "subtotal", nullable = false)
    private int subtotal;

    @Builder
    private OrderDetail(Order order, Product product, ProductOptionValue productOptionValue, int price, int quantity, int subtotal) {
        this.order = order;
        this.product = product;
        this.productOptionValue = productOptionValue;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }
}
