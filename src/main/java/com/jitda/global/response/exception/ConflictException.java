package com.jitda.global.response.exception;

public class ConflictException extends CustomException {
    public ConflictException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
