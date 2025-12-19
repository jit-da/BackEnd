package com.jitda.global.response.exception.annotation;



import com.jitda.global.response.exception.ExceptionCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerExceptionResponse {

    ExceptionCode[] value();

}
