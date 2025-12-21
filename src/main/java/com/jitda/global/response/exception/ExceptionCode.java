package com.jitda.global.response.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ExceptionCode {

    // 400 에러
    UNMATCHED_PASSWORD("E_AUTH_001", BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_FORMAT("E_AUTH_004", BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
    PASSWORD_REQUIRED("E_AUTH_005", BAD_REQUEST, "비밀번호가 필수입니다."),

    // 401 에러
    INVALID_ACCESS_TOKEN("E_AUTH_002", UNAUTHORIZED, "AccessToken이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN("E_AUTH_003", UNAUTHORIZED, "RefreshToken이 유효하지 않습니다."),
    UNAUTHORIZED_ATK_ERROR("E_UAT", UNAUTHORIZED, "AccessToken is invalid"),
    UNAUTHORIZED_RTK_ERROR("E_URT", UNAUTHORIZED, "RefreshToken is invalid"),
    EXPIRED_TOKEN_ERROR("E_EXT", UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN_ERROR("E_UST", UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    EMPTY_TOKEN_ERROR("E_EMT", UNAUTHORIZED, "JWT 토큰이 비어 있습니다."),

    // 404 에러
    NOT_FOUND_USER("E_USER_001", NOT_FOUND, "해당하는 유저가 없습니다."),
    NOT_FOUND_GRADE("E_GRADE_001", NOT_FOUND, "해당하는 등급이 없습니다."),

    // 409 에러
    DUPLICATE_EMAIL("E_USER_002", CONFLICT, "중복된 이메일입니다."),

    // 500 에러
    INTERNAL_SERVER_ERROR("E_SYS", HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),

    // Redis
    REDIS_DATA_SIZE_EXCEEDED_ERROR("E_REDIS", BAD_REQUEST, "Redis에 저장할 데이터 크기가 허용치를 초과했습니다."),
    REDIS_DATA_DELETE_ERROR("E_REDIS", BAD_REQUEST, "Redis에 저장된 데이터 삭제 중 오류가 발생했습니다."),

    // Review
    NOT_FOUND_REVIEW("E_REV_001", NOT_FOUND, "해당하는 Review가 없습니다."),
    NOT_FOUND_REVIEW_CRITERIA("E_REV_002", NOT_FOUND, "해당하는 ReviewCriteria가 없습니다."),
    NO_PERMISSION_TO_UPDATE_REVIEW("E_REV_003", FORBIDDEN, "Review 수정 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE_REVIEW("E_REV_004", FORBIDDEN, "Review 삭제 권한이 없습니다."),

    // S3
    FILE_TRANSACTION_FAIL("E_S3_001", BAD_REQUEST, "파일 변환에 실패했습니다."),
    FILE_UPLOAD_FAIL("E_S3_002", BAD_REQUEST, "파일 업로드에 실패했습니다."),
    FILE_DOWNLOAD_FAIL("E_S3_003", BAD_REQUEST, "파일 다운로드에 실패했습니다."),
    INVALID_FILE_EXTENSION("E_S3_004", BAD_REQUEST, "잘못된 파일 확장자입니다.");



    private final String code;
    private String message;
    private final HttpStatus status;

    ExceptionCode(String code, HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void updateServerErrorMessage(String message){
        this.message = message;
    }
}
