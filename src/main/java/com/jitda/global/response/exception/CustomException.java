package com.jitda.global.response.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    protected CustomException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
