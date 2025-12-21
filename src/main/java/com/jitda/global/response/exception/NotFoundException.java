package com.jitda.global.response.exception;

public class NotFoundException extends CustomException {
    public NotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
