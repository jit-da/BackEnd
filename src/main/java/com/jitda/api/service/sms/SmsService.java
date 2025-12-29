package com.jitda.api.service.sms;

public interface SmsService {
    /**
     * 인증번호 발송
     * @param phone 전화번호
     * @return 발송된 인증번호 (테스트용, 실제로는 반환하지 않음)
     */
    String sendVerificationCode(String phone);

    /**
     * 인증번호 검증
     * @param phone 전화번호
     * @param code 인증번호
     * @return 검증 성공 여부
     */
    boolean verifyCode(String phone, String code);

    /**
     * 전화번호 인증 완료 여부 확인
     * @param phone 전화번호
     * @return 인증 완료 여부
     */
    boolean isPhoneVerified(String phone);
}

