package com.jitda.domain.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "categories_large")
public class CategoryLarge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "large_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Builder
    private CategoryLarge(String name) {
        this.name = name;
    }
}
