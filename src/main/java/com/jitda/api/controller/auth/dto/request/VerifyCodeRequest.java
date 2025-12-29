package com.jitda.api.controller.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyCodeRequest {

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", 
            message = "올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)")
    private String phone;

    @NotBlank(message = "인증번호는 필수 입력 항목입니다.")
    @Size(min = 6, max = 6, message = "인증번호는 6자리여야 합니다.")
    private String code;
}


