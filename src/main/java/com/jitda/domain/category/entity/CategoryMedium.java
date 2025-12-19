package com.jitda.domain.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "categories_medium")
public class CategoryMedium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medium_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "large_id", nullable = false)
    private CategoryLarge categoryLarge;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Builder
    private CategoryMedium(CategoryLarge categoryLarge, String name) {
        this.categoryLarge = categoryLarge;
        this.name = name;
    }
}
