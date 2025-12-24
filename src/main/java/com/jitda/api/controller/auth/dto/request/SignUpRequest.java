package com.jitda.api.controller.auth.dto.request;

import com.jitda.domain.common.YN;
import com.jitda.domain.grade.entity.Grade;
import com.jitda.domain.users.entity.Provider;
import com.jitda.domain.users.entity.Role;
import com.jitda.domain.users.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 200, message = "이메일은 200자 이하여야 합니다.")
    private String email;

    private String password;

    private String passwordConfirm;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    @Size(max = 100, message = "이름은 100자 이하여야 합니다.")
    private String name;

    @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", 
            message = "올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)")
    private String phone;

    @NotNull(message = "개인정보 처리방침 동의는 필수입니다.")
    private YN agreePrivacy;

    @NotNull(message = "고유식별정보 처리 동의는 필수입니다.")
    private YN agreeUniqueInfo;

    @NotNull(message = "서비스 이용약관 동의는 필수입니다.")
    private YN agreeService;

    private YN agreeTelCarrier;

    public User toEntity(PasswordEncoder passwordEncoder, Grade grade) {
        User.UserBuilder builder = User.builder()
                .email(email)
                .provider(Provider.LOCAL)
                .role(Role.USER)
                .name(name)
                .phone(phone)
                .agreePrivacy(agreePrivacy != null ? agreePrivacy : YN.N)
                .agreeUniqueInfo(agreeUniqueInfo != null ? agreeUniqueInfo : YN.N)
                .agreeService(agreeService != null ? agreeService : YN.N)
                .agreeTelCarrier(agreeTelCarrier != null ? agreeTelCarrier : YN.N)
                .grade(grade)
                .point(0);
        
        if (password != null && !password.isBlank()) {
            builder.password(passwordEncoder.encode(password));
        }
        
        return builder.build();
    }

    public boolean isPasswordMatch() {
        return password != null && password.equals(passwordConfirm);
    }
}
