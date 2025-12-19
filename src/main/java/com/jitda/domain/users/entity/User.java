package com.jitda.domain.users.entity;

import com.jitda.domain.common.BaseEntity;
import com.jitda.domain.common.YN;
import com.jitda.domain.grade.entity.Grade;
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
@Table(name = "users")
public class User extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 200)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "oauth_provider")
    @ColumnDefault("'LOCAL'")
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'GUEST'")
    private Role role;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "agree_privacy")
    @ColumnDefault("'N'")
    private YN agreePrivacy;

    @Enumerated(EnumType.STRING)
    @Column(name = "agree_unique_info")
    @ColumnDefault("'N'")
    private YN agreeUniqueInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "agree_service")
    @ColumnDefault("'N'")
    private YN agreeService;

    @Enumerated(EnumType.STRING)
    @Column(name = "agree_tel_carrier")
    @ColumnDefault("'N'")
    private YN agreeTelCarrier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @ColumnDefault("0")
    private int point;

    @Builder
    private User(String email, String password, Provider provider, Role role, String name, String phone, Gender gender, String imageUrl, YN agreePrivacy, YN agreeUniqueInfo, YN agreeService, YN agreeTelCarrier, Grade grade, int point) {

        this.email = email;
        this.password = password;
        this.provider = provider;
        this.role = role;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.agreePrivacy = agreePrivacy;
        this.agreeUniqueInfo = agreeUniqueInfo;
        this.agreeService = agreeService;
        this.agreeTelCarrier = agreeTelCarrier;
        this.grade = grade;
        this.point = point;

    }

}


