package com.jitda.domain.grade.entity;

import com.jitda.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "grades")
public class Grade extends BaseEntity {

    @Id
    @Column(name = "grade_id", length = 36)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, length = 50)
    private GradeName name;

    @Column(name = "point_rate", precision = 5, scale = 4)
    private BigDecimal pointRate;

    @Column(name = "description")
    private String description;

    @Column(name = "monthly_volume")
    private Integer monthlyVolume;

    @Builder
    private Grade(String id, GradeName name, BigDecimal pointRate, String description, Integer monthlyVolume) {
        this.id = id;
        this.name = name;
        this.pointRate = pointRate;
        this.description = description;
        this.monthlyVolume = monthlyVolume;
    }

    public GradeName getName() {
        return name;
    }
}
