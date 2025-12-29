package com.jitda.api.controller.auth;

import com.jitda.api.controller.auth.dto.request.LoginRequest;
import com.jitda.api.controller.auth.dto.request.SendVerificationCodeRequest;
import com.jitda.api.controller.auth.dto.request.SignUpRequest;
import com.jitda.api.controller.auth.dto.request.VerifyCodeRequest;
import com.jitda.api.controller.auth.dto.response.TokenResponse;
import com.jitda.api.service.sms.SmsService;
import com.jitda.api.service.user.UserService;
import com.jitda.global.response.api.ApiResponse;
import com.jitda.global.response.exception.ExceptionCode;
import com.jitda.global.response.exception.annotation.SwaggerExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "인증")
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {

    private final UserService userService;
    private final SmsService smsService;

    @PostMapping("/sms/send")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "인증번호 발송", description = "휴대폰 인증번호 발송")
    @SwaggerExceptionResponse({ExceptionCode.SMS_SEND_FAIL})
    public ApiResponse<Void> sendVerificationCode(@RequestBody @Valid SendVerificationCodeRequest request) {
        smsService.sendVerificationCode(request.getPhone());
        return ApiResponse.noContent();
    }

    @PostMapping("/sms/verify")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "인증번호 검증", description = "휴대폰 인증번호 검증")
    @SwaggerExceptionResponse({ExceptionCode.INVALID_VERIFICATION_CODE, ExceptionCode.EXPIRED_VERIFICATION_CODE})
    public ApiResponse<Void> verifyCode(@RequestBody @Valid VerifyCodeRequest request) {
        smsService.verifyCode(request.getPhone(), request.getCode());
        return ApiResponse.noContent();
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입", description = "이메일 회원가입 (휴대폰 인증 필수)")
    @SwaggerExceptionResponse({ExceptionCode.DUPLICATE_EMAIL, ExceptionCode.INVALID_PASSWORD_FORMAT, ExceptionCode.NOT_FOUND_GRADE, ExceptionCode.PHONE_VERIFICATION_NOT_COMPLETED})
    public ApiResponse<Void> signUp(@RequestBody @Valid SignUpRequest request) {
        userService.signUp(request);
        return ApiResponse.noContent();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "로그인", description = "이메일 로그인")
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_USER, ExceptionCode.UNMATCHED_PASSWORD})
    public ApiResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        return ApiResponse.ok(userService.login(request, response));
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "토큰 재발급", description = "Access Token 재발급")
    public ApiResponse<TokenResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        return ApiResponse.ok(userService.reissue(request, response));
    }
}
