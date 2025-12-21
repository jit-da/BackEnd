package com.jitda.api.controller.auth.dto.request;

import com.jitda.domain.grade.entity.Grade;
import com.jitda.domain.users.entity.Gender;
import com.jitda.domain.users.entity.Role;
import com.jitda.domain.users.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @Email
    @NotBlank
    private String email;

    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotBlank
    private String phone;

    private Gender gender;

    private String birth;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public Gender getGender() {
        return gender;
    }

    public String getBirth() {
        return birth;
    }

    public User toEntity(PasswordEncoder passwordEncoder, Grade grade) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .nickname(nickname)
                .phone(phone)
                .gender(gender)
                .birth(birth)
                .role(Role.USER)
                .grade(grade)
                .build();
    }
}
