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

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "birth", length = 20)
    private String birth;

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

    public User(String email, String password, Provider provider, Role role, String name, String nickname, String birth, String phone, Gender gender, String imageUrl, YN agreePrivacy, YN agreeUniqueInfo, YN agreeService, YN agreeTelCarrier, Grade grade, int point) {

        this.email = email;
        this.password = password;
        this.provider = provider;
        this.role = role;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
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

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String email;
        private String password;
        private Provider provider;
        private Role role;
        private String name;
        private String nickname;
        private String birth;
        private String phone;
        private Gender gender;
        private String imageUrl;
        private YN agreePrivacy;
        private YN agreeUniqueInfo;
        private YN agreeService;
        private YN agreeTelCarrier;
        private Grade grade;
        private int point;

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder provider(Provider provider) {
            this.provider = provider;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public UserBuilder birth(String birth) {
            this.birth = birth;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public UserBuilder agreePrivacy(YN agreePrivacy) {
            this.agreePrivacy = agreePrivacy;
            return this;
        }

        public UserBuilder agreeUniqueInfo(YN agreeUniqueInfo) {
            this.agreeUniqueInfo = agreeUniqueInfo;
            return this;
        }

        public UserBuilder agreeService(YN agreeService) {
            this.agreeService = agreeService;
            return this;
        }

        public UserBuilder agreeTelCarrier(YN agreeTelCarrier) {
            this.agreeTelCarrier = agreeTelCarrier;
            return this;
        }

        public UserBuilder grade(Grade grade) {
            this.grade = grade;
            return this;
        }

        public UserBuilder point(int point) {
            this.point = point;
            return this;
        }

        public User build() {
            return new User(email, password, provider, role, name, nickname, birth, phone, gender, imageUrl, agreePrivacy, agreeUniqueInfo, agreeService, agreeTelCarrier, grade, point);
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Provider getProvider() {
        return provider;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBirth() {
        return birth;
    }

    public String getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public YN getAgreePrivacy() {
        return agreePrivacy;
    }

    public YN getAgreeUniqueInfo() {
        return agreeUniqueInfo;
    }

    public YN getAgreeService() {
        return agreeService;
    }

    public YN getAgreeTelCarrier() {
        return agreeTelCarrier;
    }

    public Grade getGrade() {
        return grade;
    }

    public int getPoint() {
        return point;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateBirth(String birth) {
        this.birth = birth;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


