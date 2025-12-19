package com.jitda.global.response.exception;

public record ExceptionResponse(
        String code,
        String Status,
        String message,
        String data
) {
    public static ExceptionResponse from(ExceptionCode exceptionCode) {
        return new ExceptionResponse(exceptionCode.getCode(), exceptionCode.getStatus().name(), exceptionCode.getMessage(), "Error");
    }

}
