package com.jitda.api.service.sms.impl;

import com.jitda.api.service.sms.SmsService;
import com.jitda.global.config.redis.service.RedisService;
import com.jitda.global.config.sms.CoolSmsConfig;
import com.jitda.global.response.exception.BadRequestException;
import com.jitda.global.response.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    private final CoolSmsConfig coolSmsConfig;
    private final RedisService redisService;
    private DefaultMessageService messageService;
    
    private static final String VERIFICATION_CODE_PREFIX = "sms:verification:";
    private static final Duration VERIFICATION_CODE_EXPIRATION = Duration.ofMinutes(5); // 5분 유효기간
    private static final int CODE_LENGTH = 6;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(
                coolSmsConfig.getApiKey(),
                coolSmsConfig.getApiSecret(),
                "https://api.coolsms.co.kr"
        );
    }

    @Override
    public String sendVerificationCode(String phone) {
        // 전화번호 형식 정규화 (하이픈 제거)
        String normalizedPhone = phone.replaceAll("-", "");

        // 인증번호 생성 (6자리)
        String verificationCode = generateVerificationCode();

        // 인증 메시지 생성
        String messageText = String.format("[Jitda] 인증번호는 [%s]입니다. 5분간 유효합니다.", verificationCode);

        try {
            // CoolSMS API 호출
            Message message = new Message();
            message.setFrom(coolSmsConfig.getSenderPhone());
            message.setTo(normalizedPhone);
            message.setText(messageText);

            messageService.sendOne(new SingleMessageSendingRequest(message));

            // Redis에 인증번호 저장 (5분 유효기간)
            String redisKey = VERIFICATION_CODE_PREFIX + normalizedPhone;
            redisService.setValues(redisKey, verificationCode, VERIFICATION_CODE_EXPIRATION);

            log.info("인증번호 발송 성공: {}", normalizedPhone);
            return verificationCode; // 테스트용으로 반환 (실제 운영에서는 제거)
        } catch (Exception e) {
            log.error("인증번호 발송 실패: {}", e.getMessage(), e);
            throw new BadRequestException(ExceptionCode.SMS_SEND_FAIL);
        }
    }

    @Override
    public boolean verifyCode(String phone, String code) {
        // 전화번호 형식 정규화
        String normalizedPhone = phone.replaceAll("-", "");

        // Redis에서 인증번호 조회
        String redisKey = VERIFICATION_CODE_PREFIX + normalizedPhone;
        String storedCode = redisService.getValues(redisKey);

        if (storedCode == null) {
            log.warn("인증번호가 존재하지 않거나 만료됨: {}", normalizedPhone);
            throw new BadRequestException(ExceptionCode.EXPIRED_VERIFICATION_CODE);
        }

        if (!storedCode.equals(code)) {
            log.warn("인증번호 불일치: {}", normalizedPhone);
            throw new BadRequestException(ExceptionCode.INVALID_VERIFICATION_CODE);
        }

        // 인증 성공 시 검증 완료 플래그 저장 (회원가입 시 사용)
        String verifiedKey = VERIFICATION_CODE_PREFIX + "verified:" + normalizedPhone;
        redisService.setValues(verifiedKey, "true", Duration.ofMinutes(10)); // 10분간 유효

        // 인증번호 삭제
        redisService.deleteValues(redisKey);

        log.info("인증번호 검증 성공: {}", normalizedPhone);
        return true;
    }

    /**
     * 인증번호 생성
     */
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 전화번호 인증 완료 여부 확인
     */
    public boolean isPhoneVerified(String phone) {
        String normalizedPhone = phone.replaceAll("-", "");
        String verifiedKey = VERIFICATION_CODE_PREFIX + "verified:" + normalizedPhone;
        String verified = redisService.getValues(verifiedKey);
        return "true".equals(verified);
    }
}

